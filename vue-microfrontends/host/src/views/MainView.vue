<template>
  <div class="main">
    <p>Main page</p>
    <p>Hi, {{ this.$store.getters.fullName }} !</p>
    <Feed />
    <p>Count from store: {{ cnt }} </p>
<!--    <button @click="updateQuantity(2)">-->
<!--      Btn-->
<!--    </button>-->
  </div>
</template>

<script>
import Feed from 'remoteApp/Feed';

export default {
  name: 'MainView',
  components: {
    Feed
  },
  computed: {
    cnt() {
      return this.$store.state.count
    }
  },
}
</script>
