import {createApp} from 'vue';
import App from './App.vue';
import {createRouter, createWebHistory} from "vue-router"

import LoginsView from "./logins/LoginsView";
import FeedView from "./feed/FeedView";

const routes = [
    {
        path: '/logins',
        name: 'ExternalLogins',
        component: LoginsView
    },
    {
        path: '/feed',
        name: 'Feed',
        component: FeedView
    },
    {
        // TODO https://github.com/vuejs/vue-router/issues/2671
        path: '/',
        redirect: '/feed',
    },
];

const router = createRouter({
    history : createWebHistory(),
    routes : routes
})

const app = createApp(App)
app.use(router);
app.mount('#app');
