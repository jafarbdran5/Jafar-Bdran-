package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CourseEntity
import com.example.data.model.CustomRuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Query("SELECT * FROM saved_courses ORDER BY createdAt DESC")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity): Long

    @Update
    suspend fun updateCourse(course: CourseEntity)

    @Query("DELETE FROM saved_courses WHERE id = :id")
    suspend fun deleteCourseById(id: Long)

    @Query("DELETE FROM saved_courses")
    suspend fun deleteAllCourses()

    // Custom Rules
    @Query("SELECT * FROM custom_rules ORDER BY createdAt DESC")
    fun getAllCustomRules(): Flow<List<CustomRuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomRule(rule: CustomRuleEntity): Long

    @Query("DELETE FROM custom_rules WHERE id = :id")
    suspend fun deleteCustomRuleById(id: Long)
}
