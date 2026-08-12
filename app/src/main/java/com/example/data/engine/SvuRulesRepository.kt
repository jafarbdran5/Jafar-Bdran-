package com.example.data.engine

import com.example.data.model.CourseTypeRule
import com.example.data.model.SvuProgram

object SvuRulesRepository {

    val OFFICIAL_SOURCE_URL = "https://www.svuonline.org/"

    /**
     * Complete repository of documented Syrian Virtual University (SVU) programs and rules.
     */
    val programs: List<SvuProgram> = listOf(
        SvuProgram(
            code = "BL",
            nameAr = "الإجازة في الحقوق (BL)",
            facultyAr = "كلية الحقوق",
            descriptionAr = "برنامج الإجازة في الحقوق بالجامعة الافتراضية السورية",
            courseTypes = listOf(
                CourseTypeRule(
                    id = "bl_theoretical",
                    nameAr = "مقرر نظري معتاد (20% مشروع + 80% امتحان)",
                    descriptionAr = "يعتمد على مشروع واحد (20%) وامتحان نهائي (80%). شرط الامتحان الأدنى 40/100 والنجاح 50/100.",
                    hasProject = true, projectMultiplier = 0.20,
                    hasEssay = false, essayMultiplier = 0.0,
                    hasExam = true, examMultiplier = 0.80, minExamMarkRequired = 40.0, minTotalPassingGrade = 50.0
                ),
                CourseTypeRule(
                    id = "bl_practical",
                    nameAr = "مقرر عملي/مستندات (30% مشروع + 70% امتحان)",
                    descriptionAr = "يعتمد على مشروع تطبيقي (30%) وامتحان كتابي (70%). شرط الامتحان الأدنى 40 والنجاح 50.",
                    hasProject = true, projectMultiplier = 0.30,
                    hasEssay = false, essayMultiplier = 0.0,
                    hasExam = true, examMultiplier = 0.70, minExamMarkRequired = 40.0, minTotalPassingGrade = 50.0
                )
            )
        ),
        SvuProgram(
            code = "BAIT",
            nameAr = "الإجازة في تقانة المعلومات (BAIT)",
            facultyAr = "كلية تقانة المعلومات",
            descriptionAr = "برنامج الإجازة في تقانة المعلومات والبرمجيات",
            courseTypes = listOf(
                CourseTypeRule(
                    id = "bait_standard",
                    nameAr = "مقرر برمجيات ونظري (20% مشروع + 80% امتحان)",
                    descriptionAr = "النظام القياسي لمقررات البرمجة والشبكات والنظرية. مشروع واحد 20%، امتحان 80%.",
                    hasProject = true, projectMultiplier = 0.20,
                    hasEssay = false, essayMultiplier = 0.0,
                    hasExam = true, examMultiplier = 0.80, minExamMarkRequired = 40.0, minTotalPassingGrade = 50.0
                ),
                CourseTypeRule(
                    id = "bait_essay",
                    nameAr = "مقرر يتضمن مقالاً (20% مشروع + 15% مقال + 65% امتحان)",
                    descriptionAr = "مقرر يتضمن مقالاً من 30 نقطة (يتحول إلى 15% من المحصلة) + مشروع (20%) + امتحان (65%).",
                    hasProject = true, projectMultiplier = 0.20,
                    hasEssay = true, essayMultiplier = 0.15, essayMaxMark = 30.0,
                    hasExam = true, examMultiplier = 0.65, minExamMarkRequired = 40.0, minTotalPassingGrade = 50.0
                ),
                CourseTypeRule(
                    id = "bait_project_heavy",
                    nameAr = "مقرر مشاريع/مختبر (30% مشروع + 70% امتحان)",
                    descriptionAr = "مقرر ذو وزن عملي مرتفع. مشروع 30% وامتحان نهائي 70%.",
                    hasProject = true, projectMultiplier = 0.30,
                    hasEssay = false, essayMultiplier = 0.0,
                    hasExam = true, examMultiplier = 0.70, minExamMarkRequired = 40.0, minTotalPassingGrade = 50.0
                )
            )
        ),
        SvuProgram(
            code = "BBA",
            nameAr = "الإجازة في إدارة الأعمال (BBA / BAE)",
            facultyAr = "كلية العلوم الإدارية والاقصاد",
            descriptionAr = "برنامج إدارة الأعمال والعلوم الاقتصادية",
            courseTypes = listOf(
                CourseTypeRule(
                    id = "bba_theoretical",
                    nameAr = "مقرر إداري معتاد (25% مشروع + 75% امتحان)",
                    descriptionAr = "مشروع واحد من 100 (يمثل 25% من المحصلة) وامتحان كتابي (75%). شرط الامتحان 40.",
                    hasProject = true, projectMultiplier = 0.25,
                    hasEssay = false, essayMultiplier = 0.0,
                    hasExam = true, examMultiplier = 0.75, minExamMarkRequired = 40.0, minTotalPassingGrade = 50.0
                ),
                CourseTypeRule(
                    id = "bba_standard_20",
                    nameAr = "مقرر قياسي 20/80 (20% مشروع + 80% امتحان)",
                    descriptionAr = "يعتمد على مشروع 20% وامتحان 80%.",
                    hasProject = true, projectMultiplier = 0.20,
                    hasEssay = false, essayMultiplier = 0.0,
                    hasExam = true, examMultiplier = 0.80, minExamMarkRequired = 40.0, minTotalPassingGrade = 50.0
                ),
                CourseTypeRule(
                    id = "bba_essay",
                    nameAr = "مقرر أبحاث مع مقال (20% مشروع + 10% مقال + 70% امتحان)",
                    descriptionAr = "يتضمن مقالاً من 30 (يعادل 10% من المحصلة) + مشروع (20%) + امتحان (70%).",
                    hasProject = true, projectMultiplier = 0.20,
                    hasEssay = true, essayMultiplier = 0.10, essayMaxMark = 30.0,
                    hasExam = true, examMultiplier = 0.70, minExamMarkRequired = 40.0, minTotalPassingGrade = 50.0
                )
            )
        ),
        SvuProgram(
            code = "TIEMD",
            nameAr = "المعهد التقاني للحواسيب والتجارة الإلكترونية (TIEMD / TIC / BIT)",
            facultyAr = "المعاهد التقانية",
            descriptionAr = "برامج المعاهد التقانية للحواسيب والشبكات والتجارة الإلكترونية",
            courseTypes = listOf(
                CourseTypeRule(
                    id = "tiemd_practical",
                    nameAr = "مقرر تقني وعملي (30% مشروع + 70% امتحان)",
                    descriptionAr = "مشروع عملي 30% وامتحان 70%. شرط الامتحان 40 والنجاح 50.",
                    hasProject = true, projectMultiplier = 0.30,
                    hasEssay = false, essayMultiplier = 0.0,
                    hasExam = true, examMultiplier = 0.70, minExamMarkRequired = 40.0, minTotalPassingGrade = 50.0
                ),
                CourseTypeRule(
                    id = "tiemd_theoretical",
                    nameAr = "مقرر تقني نظري (20% مشروع + 80% امتحان)",
                    descriptionAr = "مشروع 20% وامتحان 80%.",
                    hasProject = true, projectMultiplier = 0.20,
                    hasEssay = false, essayMultiplier = 0.0,
                    hasExam = true, examMultiplier = 0.80, minExamMarkRequired = 40.0, minTotalPassingGrade = 50.0
                )
            )
        ),
        SvuProgram(
            code = "ITE",
            nameAr = "هندسة المعلوماتية والاتصالات (ITE / ISE / BACT)",
            facultyAr = "كلية الهندسة",
            descriptionAr = "برامج الهندسة المعلوماتية وهندسة الاتصالات",
            courseTypes = listOf(
                CourseTypeRule(
                    id = "engineering_standard",
                    nameAr = "مقرر هندسي تخصصي (25% مشروع + 75% امتحان)",
                    descriptionAr = "مشروع 25% وامتحان 75%. شرط الامتحان الأدنى 50/100 والنجاح الكلي 60/100.",
                    hasProject = true, projectMultiplier = 0.25,
                    hasEssay = false, essayMultiplier = 0.0,
                    hasExam = true, examMultiplier = 0.75, minExamMarkRequired = 50.0, minTotalPassingGrade = 60.0
                ),
                CourseTypeRule(
                    id = "engineering_lab",
                    nameAr = "مقرر هندسي عملي ومخابر (40% مشروع + 60% امتحان)",
                    descriptionAr = "مشروع ومخبر 40% وامتحان 60%. شرط الامتحان الأدنى 50 والنجاح الكلي 60.",
                    hasProject = true, projectMultiplier = 0.40,
                    hasEssay = false, essayMultiplier = 0.0,
                    hasExam = true, examMultiplier = 0.60, minExamMarkRequired = 50.0, minTotalPassingGrade = 60.0
                )
            )
        ),
        SvuProgram(
            code = "MASTER",
            nameAr = "برامج الدراسات العليا والماجستير (MBA / MWS / MIBA / MSP)",
            facultyAr = "معهد الدراسات العليا",
            descriptionAr = "برامج ماجستير التأهيل والتخصص والماجستير الأكاديمي",
            courseTypes = listOf(
                CourseTypeRule(
                    id = "master_standard",
                    nameAr = "مقرر ماجستير قياسي (30% مشروع + 70% امتحان)",
                    descriptionAr = "مشروع 30% وامتحان 70%. شرط الامتحان الأدنى 50/100 وشرط النجاح الكلي 60/100.",
                    hasProject = true, projectMultiplier = 0.30,
                    hasEssay = false, essayMultiplier = 0.0,
                    hasExam = true, examMultiplier = 0.70, minExamMarkRequired = 50.0, minTotalPassingGrade = 60.0
                ),
                CourseTypeRule(
                    id = "master_essay",
                    nameAr = "مقرر ماجستير يتضمن مقالاً (25% مشروع + 15% مقال + 60% امتحان)",
                    descriptionAr = "مشروع (25%) + مقال من 30 (يعادل 15%) + امتحان (60%). شرط الامتحان 50 والنجاح 60.",
                    hasProject = true, projectMultiplier = 0.25,
                    hasEssay = true, essayMultiplier = 0.15, essayMaxMark = 30.0,
                    hasExam = true, examMultiplier = 0.60, minExamMarkRequired = 50.0, minTotalPassingGrade = 60.0
                )
            )
        ),
        SvuProgram(
            code = "CUSTOM",
            nameAr = "نظام مخصص / مقرر آخر",
            facultyAr = "إدخال مخصص",
            descriptionAr = "استخدم هذا الخيار إذا كان مقررك يعتمد توزيع أوزان خاص غير مدرج أعلاه",
            courseTypes = listOf(
                CourseTypeRule(
                    id = "custom_rule_default",
                    nameAr = "تعديل المعاملات يدوياً",
                    descriptionAr = "قم بضبط المعاملات والأوزان الخاصة بمقررك بحرية ودقة.",
                    hasProject = true, projectMultiplier = 0.20,
                    hasEssay = false, essayMultiplier = 0.0,
                    hasExam = true, examMultiplier = 0.80, minExamMarkRequired = 40.0, minTotalPassingGrade = 50.0
                )
            )
        )
    )

    fun findProgramByCode(code: String): SvuProgram? {
        return programs.find { it.code.equals(code, ignoreCase = true) }
    }

    fun getDefaultProgram(): SvuProgram = programs.first()
}
