package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents how essay marks (out of 30) are converted into the final grade.
 */
enum class EssayType {
    NONE,                   // Course has no essay
    RATIO_TO_WEIGHT         // Mark out of 30 is converted proportionally according to essayMultiplier/weight
}

/**
 * Course type calculation rule inside a program.
 */
data class CourseTypeRule(
    val id: String,
    val nameAr: String,
    val descriptionAr: String = "",
    val hasProject: Boolean = true,
    val projectMaxMark: Double = 100.0,
    val projectMultiplier: Double = 0.20, // e.g. 0.20 for 20%
    val hasEssay: Boolean = false,
    val essayMaxMark: Double = 30.0, // Strictly 30 according to SVU standards
    val essayMultiplier: Double = 0.0, // Weight contribution (e.g. 0.15 or direct points)
    val hasExam: Boolean = true,
    val examMaxMark: Double = 100.0,
    val examMultiplier: Double = 0.80, // e.g. 0.80 for 80%
    val minExamMarkRequired: Double = 40.0, // Minimum mark on exam to pass (e.g. 40 or 50)
    val minTotalPassingGrade: Double = 50.0, // Minimum total grade to pass
    val roundingDecimals: Int = 2,
    val sourceUrl: String = "https://www.svuonline.org/",
    val lastUpdated: String = "2026"
)

/**
 * SVU Program / Faculty definition.
 */
data class SvuProgram(
    val code: String,
    val nameAr: String,
    val facultyAr: String,
    val descriptionAr: String,
    val courseTypes: List<CourseTypeRule>
)

/**
 * Result status after grade evaluation.
 */
enum class PassingStatus(val labelAr: String) {
    PASSED("ناجح"),
    FAILED("راسب"),
    EXAM_MIN_NOT_MET("راسب (عدم استيفاء شرط علامة الامتحان الأدنى)"),
    UNKNOWN("بحاجة للتحقق من شروط البرنامج")
}

/**
 * Complete grade calculation breakdown.
 */
data class GradeCalculationResult(
    val rawFinalGrade: Double,
    val formattedFinalGrade: String,
    val passingStatus: PassingStatus,
    val projectContribution: Double,
    val essayContribution: Double,
    val examContribution: Double,
    val breakdownSteps: List<String>,
    val formulaDescription: String,
    val isExamMinMet: Boolean
)

/**
 * Room Entity for saved courses in "مقرراتي"
 */
@Entity(tableName = "saved_courses")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val courseName: String,
    val programCode: String,
    val programNameAr: String,
    val courseTypeId: String,
    val courseTypeNameAr: String,
    val projectMark: Double?,
    val essayMark: Double?,
    val examMark: Double?,
    val finalGrade: Double,
    val passingStatus: String,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Room Entity for custom user calculation rules
 */
@Entity(tableName = "custom_rules")
data class CustomRuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val programCode: String,
    val programNameAr: String,
    val courseTypeNameAr: String,
    val projectMultiplier: Double,
    val hasEssay: Boolean,
    val essayMultiplier: Double,
    val examMultiplier: Double,
    val minExamMark: Double,
    val minTotalMark: Double,
    val createdAt: Long = System.currentTimeMillis()
)
