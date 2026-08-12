package com.example.data.engine

import com.example.data.model.CourseTypeRule
import com.example.data.model.GradeCalculationResult
import com.example.data.model.PassingStatus
import kotlin.math.ceil
import kotlin.math.max

object SvuGradeEngine {

    /**
     * Calculates the exact final grade and step-by-step breakdown using the course rule.
     */
    fun calculateGrade(
        rule: CourseTypeRule,
        projectMark: Double?,
        essayMark: Double?,
        examMark: Double?
    ): GradeCalculationResult {
        val steps = mutableListOf<String>()

        // 1. Calculate Project Contribution
        val pMark = projectMark ?: 0.0
        val pMax = if (rule.projectMaxMark > 0) rule.projectMaxMark else 100.0
        val projectContrib = if (rule.hasProject && rule.projectMultiplier > 0) {
            val ratio = pMark / pMax
            val contrib = ratio * 100.0 * rule.projectMultiplier
            val pctStr = String.format("%.0f%%", rule.projectMultiplier * 100)
            steps.add("المشروع: $pMark من $pMax | عملية الحساب: ($pMark ÷ $pMax) × 100 × ${rule.projectMultiplier} ($pctStr) = ${String.format("%.2f", contrib)}")
            contrib
        } else 0.0

        // 2. Calculate Essay Contribution (Max 30)
        val esMark = essayMark ?: 0.0
        val esMax = 30.0 // Strictly 30
        val essayContrib = if (rule.hasEssay && rule.essayMultiplier > 0) {
            val clampedEsMark = esMark.coerceIn(0.0, esMax)
            val ratio = clampedEsMark / esMax
            val contrib = ratio * 100.0 * rule.essayMultiplier
            val pctStr = String.format("%.0f%%", rule.essayMultiplier * 100)
            steps.add("المقال: $clampedEsMark من 30 | عملية الحساب: ($clampedEsMark ÷ 30) × 100 × ${rule.essayMultiplier} ($pctStr) = ${String.format("%.2f", contrib)}")
            contrib
        } else 0.0

        // 3. Calculate Exam Contribution
        val exMark = examMark ?: 0.0
        val exMax = if (rule.examMaxMark > 0) rule.examMaxMark else 100.0
        val examContrib = if (rule.hasExam && rule.examMultiplier > 0) {
            val ratio = exMark / exMax
            val contrib = ratio * 100.0 * rule.examMultiplier
            val pctStr = String.format("%.0f%%", rule.examMultiplier * 100)
            steps.add("الامتحان: $exMark من $exMax | عملية الحساب: ($exMark ÷ $exMax) × 100 × ${rule.examMultiplier} ($pctStr) = ${String.format("%.2f", contrib)}")
            contrib
        } else 0.0

        // 4. Sum high-precision total
        val rawTotal = projectContrib + essayContrib + examContrib
        val formattedTotal = String.format("%.${rule.roundingDecimals}f", rawTotal)

        steps.add("المجموع الكلي المباشر: ${String.format("%.2f", projectContrib)} + ${String.format("%.2f", essayContrib)} + ${String.format("%.2f", examContrib)} = $formattedTotal")

        // 5. Evaluate Passing Conditions
        val isExamMinMet = if (rule.hasExam) exMark >= rule.minExamMarkRequired else true
        val isTotalPass = rawTotal >= rule.minTotalPassingGrade

        val status = when {
            !isExamMinMet -> {
                steps.add("تنبيه شرط النجاح: علامة الامتحان ($exMark) أقل من الحد الأدنى المطلوب للامتحان وهو ${rule.minExamMarkRequired}.")
                PassingStatus.EXAM_MIN_NOT_MET
            }
            isTotalPass -> {
                steps.add("المحصلة ($formattedTotal) حققت شرط النجاح التام (الحد الأدنى ${rule.minTotalPassingGrade}).")
                PassingStatus.PASSED
            }
            else -> {
                steps.add("المحصلة ($formattedTotal) أقل من الحد الأدنى للنجاح وهو ${rule.minTotalPassingGrade}.")
                PassingStatus.FAILED
            }
        }

        val formulaDesc = buildString {
            append("معادلة المقرر: ")
            val parts = mutableListOf<String>()
            if (rule.hasProject) parts.add("المشروع (${String.format("%.0f%%", rule.projectMultiplier * 100)})")
            if (rule.hasEssay) parts.add("المقال (${String.format("%.0f%%", rule.essayMultiplier * 100)})")
            if (rule.hasExam) parts.add("الامتحان (${String.format("%.0f%%", rule.examMultiplier * 100)})")
            append(parts.joinToString(" + "))
            if (rule.hasExam && rule.minExamMarkRequired > 0) {
                append(" | شرط الامتحان الأدنى: ${rule.minExamMarkRequired.toInt()}")
            }
            append(" | شرط النجاح الكلي: ${rule.minTotalPassingGrade.toInt()}")
        }

        return GradeCalculationResult(
            rawFinalGrade = rawTotal,
            formattedFinalGrade = formattedTotal,
            passingStatus = status,
            projectContribution = projectContrib,
            essayContribution = essayContrib,
            examContribution = examContrib,
            breakdownSteps = steps,
            formulaDescription = formulaDesc,
            isExamMinMet = isExamMinMet
        )
    }

    /**
     * Calculates the required exam mark to reach a desired target final grade.
     */
    fun calculateRequiredExamMark(
        rule: CourseTypeRule,
        projectMark: Double?,
        essayMark: Double?,
        targetFinalGrade: Double
    ): RequiredExamResult {
        if (!rule.hasExam || rule.examMultiplier <= 0) {
            return RequiredExamResult.Error("هذا المقرر لا يحتوي على امتحان نهائي بحسب قواعده.")
        }

        val pMark = projectMark ?: 0.0
        val pMax = if (rule.projectMaxMark > 0) rule.projectMaxMark else 100.0
        val projectContrib = if (rule.hasProject && rule.projectMultiplier > 0) {
            (pMark / pMax) * 100.0 * rule.projectMultiplier
        } else 0.0

        val esMark = essayMark ?: 0.0
        val esMax = 30.0
        val essayContrib = if (rule.hasEssay && rule.essayMultiplier > 0) {
            (esMark.coerceIn(0.0, esMax) / esMax) * 100.0 * rule.essayMultiplier
        } else 0.0

        val existingContrib = projectContrib + essayContrib
        val neededExamContrib = targetFinalGrade - existingContrib

        val examMax = if (rule.examMaxMark > 0) rule.examMaxMark else 100.0
        // neededExamMark = (neededExamContrib / (100 * examMultiplier)) * examMax
        val rawNeededExamMark = (neededExamContrib / (100.0 * rule.examMultiplier)) * examMax

        if (rawNeededExamMark > examMax) {
            return RequiredExamResult.Impossible(
                neededMark = rawNeededExamMark,
                maxAllowed = examMax,
                messageAr = "الوصول إلى هذه المحصلة غير ممكن وفق العلامات الحالية. تحتاج $${String.format("%.1f", rawNeededExamMark)} من $examMax."
            )
        }

        val effectiveNeededMark = max(rawNeededExamMark, 0.0)
        val roundedNeededMark = ceil(effectiveNeededMark * 10) / 10.0 // Round up to 1 decimal place

        val satisfiesExamMin = roundedNeededMark >= rule.minExamMarkRequired
        val recommendedMark = if (!satisfiesExamMin) {
            rule.minExamMarkRequired
        } else {
            roundedNeededMark
        }

        val note = if (!satisfiesExamMin) {
            "ملاحظة: بالرغم من أن الحساب الرياضي يعطي ${String.format("%.1f", roundedNeededMark)}، إلا أن شرط نجاح برنامج المقرر يتطلب حداً أدنى في الامتحان لا يقل عن ${rule.minExamMarkRequired.toInt()}."
        } else null

        return RequiredExamResult.Success(
            rawNeededExamMark = effectiveNeededMark,
            recommendedExamMark = recommendedMark,
            formattedMark = String.format("%.1f", recommendedMark),
            examMaxMark = examMax,
            minExamRequired = rule.minExamMarkRequired,
            additionalNote = note
        )
    }
}

sealed class RequiredExamResult {
    data class Success(
        val rawNeededExamMark: Double,
        val recommendedExamMark: Double,
        val formattedMark: String,
        val examMaxMark: Double,
        val minExamRequired: Double,
        val additionalNote: String? = null
    ) : RequiredExamResult()

    data class Impossible(
        val neededMark: Double,
        val maxAllowed: Double,
        val messageAr: String
    ) : RequiredExamResult()

    data class Error(val messageAr: String) : RequiredExamResult()
}
