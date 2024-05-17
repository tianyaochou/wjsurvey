package me.mgourd.wjsurvey

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.ManyToOne
import kotlinx.serialization.Serializable
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.springframework.data.repository.CrudRepository

interface SurveyResponseRepository : CrudRepository<SurveyResponse, Long>

@Entity
class SurveyResponse(
    @ManyToOne var survey: Survey,
    @JdbcTypeCode(SqlTypes.JSON) var responses: List<Answer>,
    @Id @GeneratedValue var id: Long? = null
)

@Serializable
class Answer(
    var key: String,
    var answer: String
)
