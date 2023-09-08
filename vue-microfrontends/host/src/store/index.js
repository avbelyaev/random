import { createStore } from 'vuex'

const store = createStore({
    state: {
        user: {
            firstName: 'John',
            lastName: 'Doe'
        },
        count: 1338,
    },
    getters: {
        fullName(state) {
            return `mr. ${state.user.firstName} ${state.user.lastName}`
        }
    },
});


export {
    store
};
