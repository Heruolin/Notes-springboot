import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'index',
    component: () => import('../views/Index.vue'),
  },
  {
    path: '/Login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
  },
  {
    path: '/Register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
  },
  {
    path: '/Test',
    name: 'Test',
    component: () => import('../views/Test.vue'),
  },
  {
    path: '/NotesShow',
    name: 'NotesShow',
    component: () => import('../components/NotesShow.vue'),
  },
  {
    path: '/NotesEdit',
    name: 'NotesEdit',
    component: () => import('../components/NotesEdit.vue'),
  },
  {
    path: '/ArchiveNotesShow',
    name: 'ArchiveNotesShow',
    component: () => import('../components/ArchiveNotesShow.vue'),
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
