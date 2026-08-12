package com.example

import com.example.data.engine.RequiredExamResult
import com.example.data.engine.SvuGradeEngine
import com.example.data.engine.SvuRulesRepository
import com.example.data.model.PassingStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testBLTheoreticalGradeCalculation() {
        val blProgram = SvuRulesRepository.findProgramByCode("BL")!!
        val theoreticalRule = blProgram.courseTypes.first() // 20% project + 80% exam

        // Project = 90, Exam = 42 => 90*0.20 + 42*0.80 = 18 + 33.60 = 51.60
        val result = SvuGradeEngine.calculateGrade(
            rule = theoreticalRule,
            projectMark = 90.0,
            essayMark = null,
            examMark = 42.0
        )

        assertEquals(51.60, result.rawFinalGrade, 0.001)
        assertEquals("51.60", result.formattedFinalGrade)
        assertEquals(PassingStatus.PASSED, result.passingStatus)
    }

    @Test
    fun testBAITEssayGradeCalculation() {
        val baitProgram = SvuRulesRepository.findProgramByCode("BAIT")!!
        val essayRule = baitProgram.courseTypes.find { it.hasEssay }!! // 20% project + 15% essay + 65% exam

        // Project = 80 (80*0.20 = 16), Essay = 24/30 ((24/30)*100*0.15 = 12), Exam = 60 (60*0.65 = 39)
        // Total = 16 + 12 + 39 = 67.0
        val result = SvuGradeEngine.calculateGrade(
            rule = essayRule,
            projectMark = 80.0,
            essayMark = 24.0,
            examMark = 60.0
        )

        assertEquals(67.0, result.rawFinalGrade, 0.001)
        assertEquals(PassingStatus.PASSED, result.passingStatus)
    }

    @Test
    fun testExamMinimumRequirementFailure() {
        val blProgram = SvuRulesRepository.findProgramByCode("BL")!!
        val theoreticalRule = blProgram.courseTypes.first() // min exam required 40

        // Project = 100 (20), Exam = 35 (35*0.80 = 28) => Total 48, but exam < 40
        val result = SvuGradeEngine.calculateGrade(
            rule = theoreticalRule,
            projectMark = 100.0,
            essayMark = null,
            examMark = 35.0
        )

        assertEquals(PassingStatus.EXAM_MIN_NOT_MET, result.passingStatus)
    }

    @Test
    fun testRequiredExamMarkCalculation() {
        val blProgram = SvuRulesRepository.findProgramByCode("BL")!!
        val theoreticalRule = blProgram.courseTypes.first() // 20% project + 80% exam

        // Project = 90 (contrib 18), Target = 60 => Need 42 points from exam
        // Needed exam mark = 42 / 0.80 = 52.5
        val reqResult = SvuGradeEngine.calculateRequiredExamMark(
            rule = theoreticalRule,
            projectMark = 90.0,
            essayMark = null,
            targetFinalGrade = 60.0
        )

        assertTrue(reqResult is RequiredExamResult.Success)
        val success = reqResult as RequiredExamResult.Success
        assertEquals(52.5, success.recommendedExamMark, 0.1)
    }
}
