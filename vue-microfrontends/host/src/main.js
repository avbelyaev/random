import {createApp, defineAsyncComponent} from 'vue';
import App from './App.vue';
import {createRouter, createWebHistory} from "vue-router";
import {store} from './store/index'

import MainView from "./views/MainView";
import AboutView from "./views/AboutView";

const routes = [
    {
        path: '/main',
        component: MainView
    },
    {
        path: '/about',
        component: AboutView
    },
    {
        path: '/remote',
        component: () => import('remoteApp/LoginsView'),
    },
    {
        path: '/',
        redirect: '/main',
    },
];

const router = createRouter({
    history : createWebHistory(),
    routes : routes
})

const app = createApp(App)
app.use(router);
app.use(store);
app.mount('#app');
