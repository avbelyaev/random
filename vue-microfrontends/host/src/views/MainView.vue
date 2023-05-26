<template>
  <div class="main">
    <p>Main page</p>
    <p>Hi, {{ $store.getters.fullName }} !</p>
    <remote-feed-element/>
<!--    <button @click="updateQuantity(2)">-->
<!--      Btn-->
<!--    </button>-->
  </div>
</template>

<script>
// import {useStoreData} from "../store";
// const {updateQuantity} = useStoreData();

export default {
name: 'MainView',

}
</script>
