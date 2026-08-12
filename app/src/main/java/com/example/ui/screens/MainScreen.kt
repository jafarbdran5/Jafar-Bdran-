package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.ui.components.DeveloperFooter
import com.example.ui.components.GradeCalculatorCard
import com.example.ui.components.HeaderBanner
import com.example.ui.components.ProgramSelectorCard
import com.example.ui.components.RulesAndSourcesTab
import com.example.ui.components.SavedCoursesTab
import com.example.ui.components.StepByStepMathDialog
import com.example.ui.components.TargetExamCard
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val activeTab by viewModel.activeTab.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    val selectedProgram by viewModel.selectedProgram.collectAsState()
    val selectedRule by viewModel.selectedRule.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val courseNameInput by viewModel.courseNameInput.collectAsState()
    val projectMarkInput by viewModel.projectMarkInput.collectAsState()
    val essayMarkInput by viewModel.essayMarkInput.collectAsState()
    val examMarkInput by viewModel.examMarkInput.collectAsState()
    val inputErrorMessage by viewModel.inputErrorMessage.collectAsState()
    val calculationResult by viewModel.calculationResult.collectAsState()
    val showMathDialog by viewModel.showMathDialog.collectAsState()

    val targetGradeInput by viewModel.targetGradeInput.collectAsState()
    val targetExamResult by viewModel.targetExamResult.collectAsState()

    val savedCourses by viewModel.savedCourses.collectAsState()
    val customRules by viewModel.customRules.collectAsState()

    // Enforce RTL Layout Direction for Arabic UX
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = activeTab == AppTab.CALCULATOR,
                        onClick = { viewModel.setTab(AppTab.CALCULATOR) },
                        modifier = Modifier.testTag("nav_tab_calculator"),
                        icon = {
                            Icon(
                                imageVector = if (activeTab == AppTab.CALCULATOR) Icons.Filled.Calculate else Icons.Outlined.Calculate,
                                contentDescription = AppTab.CALCULATOR.titleAr
                            )
                        },
                        label = { Text(AppTab.CALCULATOR.titleAr) }
                    )

                    NavigationBarItem(
                        selected = activeTab == AppTab.TARGET_EXAM,
                        onClick = { viewModel.setTab(AppTab.TARGET_EXAM) },
                        modifier = Modifier.testTag("nav_tab_target_exam"),
                        icon = {
                            Icon(
                                imageVector = if (activeTab == AppTab.TARGET_EXAM) Icons.Filled.AutoGraph else Icons.Outlined.AutoGraph,
                                contentDescription = AppTab.TARGET_EXAM.titleAr
                            )
                        },
                        label = { Text(AppTab.TARGET_EXAM.titleAr) }
                    )

                    NavigationBarItem(
                        selected = activeTab == AppTab.MY_COURSES,
                        onClick = { viewModel.setTab(AppTab.MY_COURSES) },
                        modifier = Modifier.testTag("nav_tab_my_courses"),
                        icon = {
                            Icon(
                                imageVector = if (activeTab == AppTab.MY_COURSES) Icons.Filled.ListAlt else Icons.Outlined.ListAlt,
                                contentDescription = AppTab.MY_COURSES.titleAr
                            )
                        },
                        label = { Text(AppTab.MY_COURSES.titleAr) }
                    )

                    NavigationBarItem(
                        selected = activeTab == AppTab.RULES,
                        onClick = { viewModel.setTab(AppTab.RULES) },
                        modifier = Modifier.testTag("nav_tab_rules"),
                        icon = {
                            Icon(
                                imageVector = if (activeTab == AppTab.RULES) Icons.Filled.Gavel else Icons.Outlined.Gavel,
                                contentDescription = AppTab.RULES.titleAr
                            )
                        },
                        label = { Text(AppTab.RULES.titleAr) }
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Top Header Banner
                HeaderBanner(
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = { viewModel.toggleDarkMode() }
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    AnimatedContent(
                        targetState = activeTab,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "TabContent"
                    ) { targetTab ->
                        val scrollState = rememberScrollState()

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .padding(16.dp)
                        ) {
                            when (targetTab) {
                                AppTab.CALCULATOR -> {
                                    ProgramSelectorCard(
                                        programs = viewModel.availablePrograms,
                                        selectedProgram = selectedProgram,
                                        selectedRule = selectedRule,
                                        searchQuery = searchQuery,
                                        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                                        onSelectProgram = { viewModel.selectProgram(it) },
                                        onSelectRule = { viewModel.selectRule(it) }
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    GradeCalculatorCard(
                                        rule = selectedRule,
                                        courseNameInput = courseNameInput,
                                        projectMarkInput = projectMarkInput,
                                        essayMarkInput = essayMarkInput,
                                        examMarkInput = examMarkInput,
                                        inputErrorMessage = inputErrorMessage,
                                        calculationResult = calculationResult,
                                        onCourseNameChange = { viewModel.onCourseNameChange(it) },
                                        onProjectMarkChange = { viewModel.onProjectMarkChange(it) },
                                        onEssayMarkChange = { viewModel.onEssayMarkChange(it) },
                                        onExamMarkChange = { viewModel.onExamMarkChange(it) },
                                        onShowMathDialog = { viewModel.toggleMathDialog(true) },
                                        onSaveCourse = { viewModel.saveCurrentCourseToDatabase() }
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))
                                    DeveloperFooter()
                                }

                                AppTab.TARGET_EXAM -> {
                                    ProgramSelectorCard(
                                        programs = viewModel.availablePrograms,
                                        selectedProgram = selectedProgram,
                                        selectedRule = selectedRule,
                                        searchQuery = searchQuery,
                                        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                                        onSelectProgram = { viewModel.selectProgram(it) },
                                        onSelectRule = { viewModel.selectRule(it) }
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    TargetExamCard(
                                        rule = selectedRule,
                                        projectMarkInput = projectMarkInput,
                                        essayMarkInput = essayMarkInput,
                                        targetGradeInput = targetGradeInput,
                                        targetExamResult = targetExamResult,
                                        onProjectMarkChange = { viewModel.onProjectMarkChange(it) },
                                        onEssayMarkChange = { viewModel.onEssayMarkChange(it) },
                                        onTargetGradeChange = { viewModel.onTargetGradeChange(it) }
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))
                                    DeveloperFooter()
                                }

                                AppTab.MY_COURSES -> {
                                    SavedCoursesTab(
                                        savedCourses = savedCourses,
                                        onLoadCourseForEdit = { viewModel.loadCourseForEditing(it) },
                                        onDeleteCourse = { viewModel.deleteCourse(it) },
                                        onClearAllCourses = { viewModel.clearAllSavedCourses() },
                                        onNavigateToCalculator = { viewModel.setTab(AppTab.CALCULATOR) }
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))
                                    DeveloperFooter()
                                }

                                AppTab.RULES -> {
                                    RulesAndSourcesTab(
                                        programs = viewModel.availablePrograms,
                                        customRules = customRules,
                                        onAddCustomRule = { name, type, proj, hasEs, es, ex, minEx, minTot ->
                                            viewModel.addCustomUserRule(name, type, proj, hasEs, es, ex, minEx, minTot)
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))
                                    DeveloperFooter()
                                }
                            }
                        }
                    }
                }
            }

            // Step by step math breakdown dialog
            if (showMathDialog && calculationResult != null) {
                StepByStepMathDialog(
                    result = calculationResult!!,
                    onDismiss = { viewModel.toggleMathDialog(false) }
                )
            }
        }
    }
}
