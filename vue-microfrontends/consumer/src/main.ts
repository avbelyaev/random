import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'

import AboutView from "./views/AboutView.vue";
import HomeView from "./views/HomeView.vue";


// register remote component globally
Vue.component('RemoteExternalLoginsView', () => import('remoteApp/ExternalLoginsView'));
import RemoteExternalLoginsView from 'remoteApp/ExternalLoginsView';

Vue.use(VueRouter)
const router = new VueRouter({
    routes: [
        {
            path: '/home',
            component: HomeView
        },
        {
            path: '/about',
            component: AboutView
        },
        {
            path: '/remote',
            component: RemoteExternalLoginsView, //() => import('remoteApp/ExternalLoginsView'),
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
