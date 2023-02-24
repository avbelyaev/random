import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'

import ExternalLoginsView from "./views/ExternalLoginsView.vue";
import HomeView from "./views/HomeView.vue";


Vue.use(VueRouter)

const router = new VueRouter({
  routes: [
    {
      path: '/extlogins*',
      name: 'ExternalLogins',
      component: ExternalLoginsView
    },
    {
      path: '/home',
      name: 'Home',
      component: HomeView
    },
    {
      path: '/',
      redirect: '/home',
    },
  ]
})

new Vue({
  render: (h) => h(App),
  router
}).$mount('#app')
