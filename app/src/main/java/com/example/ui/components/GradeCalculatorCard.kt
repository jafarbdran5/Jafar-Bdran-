package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CourseTypeRule
import com.example.data.model.GradeCalculationResult
import com.example.data.model.PassingStatus

@Composable
fun GradeCalculatorCard(
    rule: CourseTypeRule,
    courseNameInput: String,
    projectMarkInput: String,
    essayMarkInput: String,
    examMarkInput: String,
    inputErrorMessage: String?,
    calculationResult: GradeCalculationResult?,
    onCourseNameChange: (String) -> Unit,
    onProjectMarkChange: (String) -> Unit,
    onEssayMarkChange: (String) -> Unit,
    onExamMarkChange: (String) -> Unit,
    onShowMathDialog: () -> Unit,
    onSaveCourse: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "2. إدخال علامات المقرر",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Course Name Optional Input
            OutlinedTextField(
                value = courseNameInput,
                onValueChange = onCourseNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("course_name_input"),
                label = { Text("اسم المقرر (اختياري للحفظ)") },
                placeholder = { Text("مثال: قانون دستوري / برمجة 1") },
                leadingIcon = { Icon(Icons.Default.Book, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 1) "المشروع" (Project Input - strictly max 1 project, labeled "المشروع")
            if (rule.hasProject) {
                val projWeightStr = String.format("%.0f%%", rule.projectMultiplier * 100)
                OutlinedTextField(
                    value = projectMarkInput,
                    onValueChange = { newVal ->
                        // Allow empty or numeric up to projectMaxMark
                        val num = newVal.toDoubleOrNull()
                        if (newVal.isEmpty() || (num != null && num in 0.0..rule.projectMaxMark)) {
                            onProjectMarkChange(newVal)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("project_mark_input"),
                    label = { Text("المشروع (من ${rule.projectMaxMark.toInt()}) — الوزن الحسابي: $projWeightStr") },
                    placeholder = { Text("أدخل علامة المشروع بين 0 و ${rule.projectMaxMark.toInt()}") },
                    leadingIcon = { Icon(Icons.Default.Assignment, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // 2) "المقال" (Essay Input - strictly range 0 to 30! ONLY shown if course rule has essay)
            if (rule.hasEssay) {
                val essayWeightStr = String.format("%.0f%%", rule.essayMultiplier * 100)
                OutlinedTextField(
                    value = essayMarkInput,
                    onValueChange = { newVal ->
                        val num = newVal.toDoubleOrNull()
                        if (newVal.isEmpty() || (num != null && num in 0.0..30.0)) {
                            onEssayMarkChange(newVal)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("essay_mark_input"),
                    label = { Text("المقال (من 30 فقط) — الوزن الحسابي: $essayWeightStr") },
                    placeholder = { Text("علامة المقال بين 0 و 30 فقط") },
                    leadingIcon = { Icon(Icons.Default.EditNote, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    supportingText = {
                        Text(
                            text = "ملاحظة: علامة المقال محددة رسمياً من 0 إلى 30، ويقوم التطبيق بتحويلها رياضياً وفق القاعدة.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // 3) "الامتحان" (Exam Input - 0 to 100)
            if (rule.hasExam) {
                val examWeightStr = String.format("%.0f%%", rule.examMultiplier * 100)
                OutlinedTextField(
                    value = examMarkInput,
                    onValueChange = { newVal ->
                        val num = newVal.toDoubleOrNull()
                        if (newVal.isEmpty() || (num != null && num in 0.0..rule.examMaxMark)) {
                            onExamMarkChange(newVal)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("exam_mark_input"),
                    label = { Text("الامتحان (من ${rule.examMaxMark.toInt()}) — الوزن الحسابي: $examWeightStr") },
                    placeholder = { Text("أدخل علامة الامتحان بين 0 و ${rule.examMaxMark.toInt()}") },
                    leadingIcon = { Icon(Icons.Default.Quiz, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            // Error Message Alert
            if (!inputErrorMessage.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = inputErrorMessage,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }

            // Display Calculation Result Banner
            if (calculationResult != null) {
                Spacer(modifier = Modifier.height(18.dp))
                ResultCardContent(
                    result = calculationResult,
                    onShowMathDialog = onShowMathDialog,
                    onSaveCourse = onSaveCourse
                )
            }
        }
    }
}

@Composable
private fun ResultCardContent(
    result: GradeCalculationResult,
    onShowMathDialog: () -> Unit,
    onSaveCourse: () -> Unit
) {
    val isPassed = result.passingStatus == PassingStatus.PASSED
    val isExamMinFailed = result.passingStatus == PassingStatus.EXAM_MIN_NOT_MET

    val statusBg = when (result.passingStatus) {
        PassingStatus.PASSED -> Color(0xFFD1FAE5)
        PassingStatus.FAILED -> Color(0xFFFEE2E2)
        PassingStatus.EXAM_MIN_NOT_MET -> Color(0xFFFFEDD5)
        PassingStatus.UNKNOWN -> MaterialTheme.colorScheme.surfaceVariant
    }

    val statusTextClr = when (result.passingStatus) {
        PassingStatus.PASSED -> Color(0xFF065F46)
        PassingStatus.FAILED -> Color(0xFF991B1B)
        PassingStatus.EXAM_MIN_NOT_MET -> Color(0xFF9A3412)
        PassingStatus.UNKNOWN -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.5.dp,
                color = statusTextClr.copy(alpha = 0.4f),
                shape = RoundedCornerShape(16.dp)
            ),
        color = statusBg.copy(alpha = 0.4f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "المحصلة النهائية للمقرر",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${result.formattedFinalGrade} / 100",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 36.sp,
                    color = statusTextClr
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Status Chip
            Surface(
                shape = CircleShape,
                color = statusBg
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isPassed) Icons.Default.CheckCircle else if (isExamMinFailed) Icons.Default.Warning else Icons.Default.Error,
                        contentDescription = null,
                        tint = statusTextClr,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = result.passingStatus.labelAr,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = statusTextClr
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Formula summary note
            Text(
                text = result.formulaDescription,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onShowMathDialog,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("show_math_breakdown_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "كيف تم حساب النتيجة؟",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Button(
                    onClick = onSaveCourse,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("save_course_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "حفظ المقرّر",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
