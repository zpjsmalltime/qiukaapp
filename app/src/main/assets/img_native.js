'use strict';

var imgList = document.querySelectorAll('img[img_width]');
var bodyWidth = document.documentElement.offsetWidth;
var ratio = void 0,
    imgArr = [];

var _loop = function _loop(i, len) {
  ratio = imgList[i].getAttribute('img_width') / bodyWidth;
  imgArr.push(imgList[i].getAttribute('src'));
  imgList[i].style.wdith = '100%';
  imgList[i].style.height = imgList[i].getAttribute('img_height') / ratio + 'px';
  imgList[i].addEventListener('click', function () {
    nativePicture(imgArr.join(','), i);
  });
};

for (var i = 0, len = imgList.length; i < len; i++) {
  _loop(i, len);
}

function nativePicture(arr, index) {
  window.qk.nativePicture(arr, index.toString());
}