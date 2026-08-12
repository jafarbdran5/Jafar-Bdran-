package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.engine.RequiredExamResult
import com.example.data.engine.SvuGradeEngine
import com.example.data.engine.SvuRulesRepository
import com.example.data.local.AppDatabase
import com.example.data.local.CourseRepository
import com.example.data.model.CourseEntity
import com.example.data.model.CourseTypeRule
import com.example.data.model.CustomRuleEntity
import com.example.data.model.GradeCalculationResult
import com.example.data.model.SvuProgram
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val titleAr: String) {
    CALCULATOR("احسب مقرر"),
    TARGET_EXAM("كم أحتاج في الامتحان؟"),
    MY_COURSES("مقرراتي"),
    RULES("المصادر واللوائح")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CourseRepository

    val savedCourses: StateFlow<List<CourseEntity>>
    val customRules: StateFlow<List<CustomRuleEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).courseDao()
        repository = CourseRepository(dao)
        savedCourses = repository.allCourses.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        customRules = repository.allCustomRules.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    // UI Navigation State
    private val _activeTab = MutableStateFlow(AppTab.CALCULATOR)
    val activeTab: StateFlow<AppTab> = _activeTab.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun setTab(tab: AppTab) {
        _activeTab.value = tab
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    // Program Selection State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val availablePrograms: List<SvuProgram> = SvuRulesRepository.programs

    private val _selectedProgram = MutableStateFlow<SvuProgram>(SvuRulesRepository.getDefaultProgram())
    val selectedProgram: StateFlow<SvuProgram> = _selectedProgram.asStateFlow()

    private val _selectedRule = MutableStateFlow<CourseTypeRule>(
        SvuRulesRepository.getDefaultProgram().courseTypes.first()
    )
    val selectedRule: StateFlow<CourseTypeRule> = _selectedRule.asStateFlow()

    // Form Input States
    private val _courseNameInput = MutableStateFlow("")
    val courseNameInput: StateFlow<String> = _courseNameInput.asStateFlow()

    private val _projectMarkInput = MutableStateFlow("")
    val projectMarkInput: StateFlow<String> = _projectMarkInput.asStateFlow()

    private val _essayMarkInput = MutableStateFlow("")
    val essayMarkInput: StateFlow<String> = _essayMarkInput.asStateFlow()

    private val _examMarkInput = MutableStateFlow("")
    val examMarkInput: StateFlow<String> = _examMarkInput.asStateFlow()

    // Calculation Result
    private val _calculationResult = MutableStateFlow<GradeCalculationResult?>(null)
    val calculationResult: StateFlow<GradeCalculationResult?> = _calculationResult.asStateFlow()

    private val _showMathDialog = MutableStateFlow(false)
    val showMathDialog: StateFlow<Boolean> = _showMathDialog.asStateFlow()

    private val _inputErrorMessage = MutableStateFlow<String?>(null)
    val inputErrorMessage: StateFlow<String?> = _inputErrorMessage.asStateFlow()

    // Target Exam Tool States
    private val _targetGradeInput = MutableStateFlow("60")
    val targetGradeInput: StateFlow<String> = _targetGradeInput.asStateFlow()

    private val _targetExamResult = MutableStateFlow<RequiredExamResult?>(null)
    val targetExamResult: StateFlow<RequiredExamResult?> = _targetExamResult.asStateFlow()

    // Actions
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun selectProgram(program: SvuProgram) {
        _selectedProgram.value = program
        val defaultRule = program.courseTypes.firstOrNull() ?: SvuRulesRepository.getDefaultProgram().courseTypes.first()
        _selectedRule.value = defaultRule
        recalculateCurrentGrade()
    }

    fun selectRule(rule: CourseTypeRule) {
        _selectedRule.value = rule
        recalculateCurrentGrade()
    }

    fun onCourseNameChange(value: String) {
        _courseNameInput.value = value
    }

    fun onProjectMarkChange(value: String) {
        _projectMarkInput.value = value
        validateAndCalculate()
    }

    fun onEssayMarkChange(value: String) {
        _essayMarkInput.value = value
        validateAndCalculate()
    }

    fun onExamMarkChange(value: String) {
        _examMarkInput.value = value
        validateAndCalculate()
    }

    fun onTargetGradeChange(value: String) {
        _targetGradeInput.value = value
        calculateTargetExam()
    }

    private fun validateAndCalculate() {
        _inputErrorMessage.value = null

        val pVal = _projectMarkInput.value.toDoubleOrNull()
        val esVal = _essayMarkInput.value.toDoubleOrNull()
        val exVal = _examMarkInput.value.toDoubleOrNull()

        val rule = _selectedRule.value

        // Validate range limits
        if (_projectMarkInput.value.isNotEmpty() && (pVal == null || pVal < 0 || pVal > rule.projectMaxMark)) {
            _inputErrorMessage.value = "علامة المشروع يجب أن تكون بين 0 و ${rule.projectMaxMark.toInt()}."
            return
        }

        if (rule.hasEssay && _essayMarkInput.value.isNotEmpty()) {
            if (esVal == null || esVal < 0 || esVal > 30.0) {
                _inputErrorMessage.value = "علامة المقال يجب أن تكون بين 0 و 30 فقط."
                return
            }
        }

        if (_examMarkInput.value.isNotEmpty() && (exVal == null || exVal < 0 || exVal > rule.examMaxMark)) {
            _inputErrorMessage.value = "علامة الامتحان يجب أن تكون بين 0 و ${rule.examMaxMark.toInt()}."
            return
        }

        val result = SvuGradeEngine.calculateGrade(
            rule = rule,
            projectMark = pVal,
            essayMark = esVal,
            examMark = exVal
        )
        _calculationResult.value = result
    }

    private fun recalculateCurrentGrade() {
        validateAndCalculate()
        calculateTargetExam()
    }

    fun calculateTargetExam() {
        val target = _targetGradeInput.value.toDoubleOrNull() ?: 60.0
        val pVal = _projectMarkInput.value.toDoubleOrNull() ?: 0.0
        val esVal = _essayMarkInput.value.toDoubleOrNull() ?: 0.0

        val res = SvuGradeEngine.calculateRequiredExamMark(
            rule = _selectedRule.value,
            projectMark = pVal,
            essayMark = esVal,
            targetFinalGrade = target
        )
        _targetExamResult.value = res
    }

    fun toggleMathDialog(show: Boolean) {
        _showMathDialog.value = show
    }

    // Saved Courses Persistence Actions
    fun saveCurrentCourseToDatabase() {
        val result = _calculationResult.value ?: return
        val courseName = _courseNameInput.value.ifBlank { "مقرر ${_selectedProgram.value.code}" }

        viewModelScope.launch {
            val entity = CourseEntity(
                courseName = courseName,
                programCode = _selectedProgram.value.code,
                programNameAr = _selectedProgram.value.nameAr,
                courseTypeId = _selectedRule.value.id,
                courseTypeNameAr = _selectedRule.value.nameAr,
                projectMark = _projectMarkInput.value.toDoubleOrNull(),
                essayMark = if (_selectedRule.value.hasEssay) _essayMarkInput.value.toDoubleOrNull() else null,
                examMark = _examMarkInput.value.toDoubleOrNull(),
                finalGrade = result.rawFinalGrade,
                passingStatus = result.passingStatus.labelAr
            )
            repository.insertCourse(entity)
            _activeTab.value = AppTab.MY_COURSES
        }
    }

    fun deleteCourse(course: CourseEntity) {
        viewModelScope.launch {
            repository.deleteCourseById(course.id)
        }
    }

    fun clearAllSavedCourses() {
        viewModelScope.launch {
            repository.deleteAllCourses()
        }
    }

    fun loadCourseForEditing(course: CourseEntity) {
        // Find matching program
        val program = SvuRulesRepository.findProgramByCode(course.programCode) ?: SvuRulesRepository.getDefaultProgram()
        _selectedProgram.value = program

        val rule = program.courseTypes.find { it.id == course.courseTypeId } ?: program.courseTypes.first()
        _selectedRule.value = rule

        _courseNameInput.value = course.courseName
        _projectMarkInput.value = course.projectMark?.let { String.format("%.1f", it) } ?: ""
        _essayMarkInput.value = course.essayMark?.let { String.format("%.1f", it) } ?: ""
        _examMarkInput.value = course.examMark?.let { String.format("%.1f", it) } ?: ""

        recalculateCurrentGrade()
        _activeTab.value = AppTab.CALCULATOR
    }

    fun addCustomUserRule(
        programName: String,
        courseTypeName: String,
        projectPct: Double,
        hasEssay: Boolean,
        essayPct: Double,
        examPct: Double,
        minExamMark: Double,
        minTotalMark: Double
    ) {
        viewModelScope.launch {
            val rule = CustomRuleEntity(
                programCode = "CUSTOM",
                programNameAr = programName.ifBlank { "نظام مخصص" },
                courseTypeNameAr = courseTypeName.ifBlank { "مقرر مخصص" },
                projectMultiplier = projectPct / 100.0,
                hasEssay = hasEssay,
                essayMultiplier = essayPct / 100.0,
                examMultiplier = examPct / 100.0,
                minExamMark = minExamMark,
                minTotalMark = minTotalMark
            )
            repository.insertCustomRule(rule)
        }
    }
}
