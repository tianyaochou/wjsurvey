import { createRouter, createWebHistory } from 'vue-router'
import SurveyView from '../view/SurveyView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/survey/:id',
      component: SurveyView
    }
  ]
})

export default router
