const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const {VueLoaderPlugin} = require("vue-loader");

const babelOptions = {
  presets: ['@babel/preset-env'],
  plugins: ['@babel/plugin-syntax-dynamic-import'],
};

module.exports = {
  entry: ['@babel/polyfill', './src/main.ts'],
  mode: 'development',
  output: {
    publicPath: `http://localhost:3000/`,
  },
  devServer: {
    host: '127.0.0.1',
    port: 3000,
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        loader: 'vue-loader'
      },
      {
        // Javascript files other than vue
        test: /\.js$/,
        loader: 'babel-loader',
        exclude: file => /node_modules/.test(file) && !/\.vue\.js/.test(file),
        options: babelOptions,
      },
      // Typescript
      {
        test: /\.ts$/,
        exclude: /node_modules|vue\/src/,
        use: [
          {
            loader: 'babel-loader',
            options: babelOptions,
          },
          {
            loader: 'ts-loader',
            options: {
              appendTsSuffixTo: [/\.vue$/],
              onlyCompileBundledFiles: true,
            },
          },
        ],
      },
      {
        test: /\.css$/,
        use: [
          'vue-style-loader',
          {
            loader: 'css-loader',
            options: {
              modules: {
                mode: 'local', // Using `local` value has same effect like using `modules: true`
                localIdentName: '[local]_[hash:base64:8]',
              },
            },
          },
        ],
      },
    ],
  },
  devtool: 'eval-cheap-module-source-map',
  plugins: [
    new VueLoaderPlugin(),
    new webpack.HotModuleReplacementPlugin(),
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, 'index.html'),
      inject: true,
      chunks: ['main'],
      title: 'Vue Facebook buttons',
      hash: true,
    })
  ],
  resolve: {
    alias: {
      vue$: 'vue/dist/vue.esm.js',
      '@': path.resolve(__dirname, './src'),
    },
    extensions: ['*', '.ts', '.js', '.vue', '.json'],
  },
}