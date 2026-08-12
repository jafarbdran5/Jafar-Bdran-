package com.example.ui.components

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
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
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
import com.example.data.engine.RequiredExamResult
import com.example.data.model.CourseTypeRule

@Composable
fun TargetExamCard(
    rule: CourseTypeRule,
    projectMarkInput: String,
    essayMarkInput: String,
    targetGradeInput: String,
    targetExamResult: RequiredExamResult?,
    onProjectMarkChange: (String) -> Unit,
    onEssayMarkChange: (String) -> Unit,
    onTargetGradeChange: (String) -> Unit,
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoGraph,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "أداة: كم أحتاج في الامتحان؟",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "احسب علامة الامتحان المطلوبة للوصول إلى المحصلة المرغوبة",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Inputs Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Project Mark
                if (rule.hasProject) {
                    OutlinedTextField(
                        value = projectMarkInput,
                        onValueChange = onProjectMarkChange,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("target_project_input"),
                        label = { Text("علامة المشروع") },
                        placeholder = { Text("0 - 100") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Essay Mark (if enabled)
                if (rule.hasEssay) {
                    OutlinedTextField(
                        value = essayMarkInput,
                        onValueChange = onEssayMarkChange,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("target_essay_input"),
                        label = { Text("علامة المقال (0-30)") },
                        placeholder = { Text("0 - 30") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Target Final Grade Input Slider & TextField
            Text(
                text = "المحصلة المرغوبة التي تريد الوصول إليها:",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val targetNum = targetGradeInput.toFloatOrNull() ?: 60f
                Slider(
                    value = targetNum.coerceIn(0f, 100f),
                    onValueChange = { newVal ->
                        onTargetGradeChange(newVal.toInt().toString())
                    },
                    valueRange = 0f..100f,
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = targetGradeInput,
                    onValueChange = onTargetGradeChange,
                    modifier = Modifier
                        .width(90.dp)
                        .testTag("target_grade_input"),
                    label = { Text("المحصلة") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display Result State
            if (targetExamResult != null) {
                when (targetExamResult) {
                    is RequiredExamResult.Success -> {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = 1.5.dp,
                                    color = Color(0xFF059669),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            color = Color(0xFFD1FAE5).copy(alpha = 0.5f)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "علامة الامتحان المطلوبة في ${rule.nameAr}:",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = Color(0xFF065F46)
                                    )
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${targetExamResult.formattedMark} / ${targetExamResult.examMaxMark.toInt()}",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 32.sp,
                                        color = Color(0xFF047857)
                                    )
                                )

                                if (targetExamResult.additionalNote != null) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = targetExamResult.additionalNote,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = Color(0xFF065F46),
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }
                    }

                    is RequiredExamResult.Impossible -> {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = 1.5.dp,
                                    color = Color(0xFFDC2626),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            color = Color(0xFFFEE2E2).copy(alpha = 0.6f)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color(0xFF991B1B),
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "الوصول إلى هذه المحصلة غير ممكن وفق العلامات الحالية.",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF991B1B)
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = targetExamResult.messageAr,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF7F1D1D)
                                        )
                                    )
                                }
                            }
                        }
                    }

                    is RequiredExamResult.Error -> {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = targetExamResult.messageAr,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                }
            }
        }
    }
}
