import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'

import FeedView from "@/feed/FeedView.vue";
import LoginsView from "@/logins/LoginsView.vue";


Vue.use(VueRouter)

const router = new VueRouter({
  routes: [
    {
      path: '/logins*',
      name: 'ExternalLogins',
      component: LoginsView
    },
    {
      path: '/feed',
      name: 'Feed',
      component: FeedView
    },
    {
      path: '/',
      redirect: '/feed',
    },
  ]
})

new Vue({
  render: (h) => h(App),
  router
}).$mount('#app')
