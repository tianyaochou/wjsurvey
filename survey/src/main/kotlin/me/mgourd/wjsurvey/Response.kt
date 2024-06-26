package me.mgourd.wjsurvey

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import kotlinx.serialization.Serializable
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.springframework.data.repository.CrudRepository

interface SurveyResponseRepository : CrudRepository<SurveyResponse, Long>

@Entity
@Table(name = "response")
//@Serializable
class SurveyResponse(
    @ManyToOne var survey: Survey = Survey(),
    @JdbcTypeCode(SqlTypes.JSON) var responses: List<Answer> = listOf(),
    @Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "response_id_seq") @SequenceGenerator(name="response_id_seq", sequenceName="response_seq", allocationSize=1) var id: Long? = null
)

@Serializable
class Answer(
    var key: String,
    var answer: String
)
