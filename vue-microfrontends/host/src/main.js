import {createApp, defineAsyncComponent} from 'vue';
import App from './App.vue';
import {createRouter, createWebHistory} from "vue-router";

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
        redirect: '/home',
    },
];

const router = createRouter({
    history : createWebHistory(),
    routes : routes
})

const app = createApp(App)
app.use(router);

const RemoteFeed = defineAsyncComponent(() => import('remoteApp/Feed'));
app.component('remote-feed-element', RemoteFeed);

app.mount('#app');
