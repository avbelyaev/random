import { createStore } from 'vuex'
// import {reactivex, toRefs} from "vue";

// import { reactive } from 'vue'

// export const state = reactive({
//     data: {
//         firstName: 'John',
//         lastName: 'Doe'
//     },
//     getters: {
//         fullName: function (data) {
//             return `mr. ${data.firstName} ${data.lastName}`
//         }
//     }
// })
const store = createStore({
    state() {
       return {
           count: 1338
       };
    },
    storeX: {
        firstName: 'John',
        lastName: 'Doe'
    },
    getters: {
        fullName: function (storeX) {
            return `mr. ${storeX.firstName} ${storeX.lastName}`
        }
    },
});

const doFoo = () => {
    return 'foobar';
}

export {
    store,
    doFoo
};
// const useStoreData = () => {
//     const updateQuantity = (qty) => {
//         state.data.firstName += `${qty}`;
//     }
//     return {
//         state,
//         updateQuantity,
//     }
// }

// export {
//     state,
//     // useStoreData
// };

// const state = reactive({
//     data: {
//         quantity: 1,
//     },
// })

