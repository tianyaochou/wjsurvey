package me.mgourd.wjsurvey

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.function.ServerResponse

@SpringBootApplication
@RestController
class WjsurveyApplication(
        val surveyRepo: SurveyRepository,
        val responseRepo: SurveyResponseRepository
) {
    @GetMapping("/survey/{id}")
    fun getSurvey(@PathVariable id: Long): Survey {
        return surveyRepo.findById(id).get()
    }

    @PostMapping("/survey")
    @ResponseStatus(HttpStatus.CREATED)
    fun createSurvey(@RequestBody survey: Survey): Survey {
        if (survey.validateQuestionKeys()) {
            surveyRepo.save(survey)
            return survey
        } else {
            throw IllegalStateException("Invalid Questions")
        }
    }

    @PatchMapping("/survey/{id}")
    fun updateSurvey(@PathVariable id: Long, @RequestBody survey: Survey) {
        val old_survey = surveyRepo.findById(id).get()
        survey.id = old_survey.id
        surveyRepo.save(survey)
    }

    @DeleteMapping("/survey/{id}")
    fun deleteSurvey(@PathVariable id: Long) {
        surveyRepo.deleteById(id)
    }

    @PostMapping("/survey/{id}/response")
    fun postResponse(@PathVariable id: Long, @RequestBody response: SurveyResponse) {
        val survey = surveyRepo.findById(id).get()
        response.survey = survey
        if (survey.validateResponse(response)) {

        } else {
            throw IllegalStateException("Invalid Response")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<WjsurveyApplication>(*args)
}
