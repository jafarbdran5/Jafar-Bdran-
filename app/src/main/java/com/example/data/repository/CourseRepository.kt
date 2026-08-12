package com.example.data.local

import com.example.data.model.CourseEntity
import com.example.data.model.CustomRuleEntity
import kotlinx.coroutines.flow.Flow

class CourseRepository(private val courseDao: CourseDao) {

    val allCourses: Flow<List<CourseEntity>> = courseDao.getAllCourses()
    val allCustomRules: Flow<List<CustomRuleEntity>> = courseDao.getAllCustomRules()

    suspend fun insertCourse(course: CourseEntity): Long {
        return courseDao.insertCourse(course)
    }

    suspend fun updateCourse(course: CourseEntity) {
        courseDao.updateCourse(course)
    }

    suspend fun deleteCourseById(id: Long) {
        courseDao.deleteCourseById(id)
    }

    suspend fun deleteAllCourses() {
        courseDao.deleteAllCourses()
    }

    suspend fun insertCustomRule(rule: CustomRuleEntity): Long {
        return courseDao.insertCustomRule(rule)
    }

    suspend fun deleteCustomRuleById(id: Long) {
        courseDao.deleteCustomRuleById(id)
    }
}
