// 2. This code loads the IFrame Player API code asynchronously.
var tag = document.createElement('script');

tag.src = "https://www.youtube.com/iframe_api";
var firstScriptTag = document.getElementsByTagName('script')[0];
firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

// 3. This function creates an <iframe> (and YouTube player)
//    after the API code downloads.
if(typeof videoId === 'undefined') var videoId = 'FlsCjmMhFmw'; //youtube rewind inseted if no videoId found
var player;
function onYouTubeIframeAPIReady() {
  player = new YT.Player('player', {
    height: '390',
    width: '640',
    videoId: videoId,
    events: {
      'onReady': onPlayerReady
    }
  });
}
// params for new YT.Player()
//    playerVars: {
//        start: 25,
//        end: 30
//    },

// 4. The API will call this function when the video player is ready.
function onPlayerReady(event) {
  event.target.playVideo();
}

//trying to load and play video
function onClickLoadVideo(startTime, duration){
  console.log("onClickLoadVideo called");
  player.loadVideoById({
      'videoId': videoId,
      'startSeconds': startTime,
      'endSeconds': startTime+duration,
      'suggestedQuality': 'default'
  });
}