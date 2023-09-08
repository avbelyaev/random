import { createStore } from 'vuex'

const store = createStore({
    state: {
        user: {
            firstName: 'John',
            lastName: 'Doe'
        },
        score: 1337,
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
