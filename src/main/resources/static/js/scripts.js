String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};

$(".submit-write button[type=submit]").click(addAnswer);
function addAnswer(e) {
  e.preventDefault();

  var queryString = $("form[name=answer]").serialize();
  console.log("queryString : " + queryString);

  var url = "/api" + $(".submit-write").attr("action");
  console.log("url : " + url);

  $.ajax({
    type : 'post',
    url : url,
    data : queryString,
    dataType : 'json',
    error: function () {
      alert("error");
    },
    success : function (data, status) {
      console.log(data);
      var answerTemplate = $("#answerTemplate").html();
      var template = answerTemplate.format(data.writer, data.time, data.contents, data.seq, data.userSeq, data.articleSeq);
      $(".qna-comment-slipp-articles").prepend(template);
      $("textarea[name=contents]").val("");
    }
  });
}

$(".delete-answer-form button[type='submit']").click(deleteAnswer);
function deleteAnswer(e) {
  e.preventDefault();

  var deleteBtn = $(this);

  var queryString = $("form[name=delete-answer]").serialize();
  console.log("queryString : " + queryString);

  var url = "/api" + $(".delete-answer-form").attr("action");
  console.log("url : " + url);

  $.ajax({
    type : 'delete',
    url : url,
    data : queryString,
    dataType : 'json',
    error : function (xhr, status) {
      console.log("error");
    },
    success : function (data, status) {
      console.log(data);
      if (data.valid) {
        deleteBtn.closest("article").remove();
      } else {
        alert(data.errorMessage);
      }
    }
  });
}