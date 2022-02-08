function makeNewMemo(memoObject) {
    let newMemo = document.createElement("tr");

    newMemo.setAttribute("class", "text-center");
    newMemo.setAttribute("style", "background-color: white;");
    newMemo.innerHTML = `
                        <td>${memoObject.formattedCreatedDate}</td>
                        <td>${memoObject.writerId}</a></td>
                        <td>${memoObject.content}</td>
                    `
    return newMemo;
}

window.onload = function () {
    let memoList = document.getElementById("memoList");

    axios.get(`/memo/list`).then(({data}) => {
        let memos = data.memos;
        let size = memos.length;

        for (let i = 0; i < size; i++) {
            let memo = memos[i];
            let memoObject = {
                formattedCreatedDate: memo.formattedCreatedDate,
                writerId: memo.writerId,
                content: memo.content,
            }

            memoList.insertBefore(makeNewMemo(memoObject), memoList.firstChild);
        }
    });

    let memoBtn = document.getElementById("memoAddButton");

    if (memoBtn !== null) {
        document.getElementById("memoAddButton").onclick = function () {
            let content = document.getElementById("memoContent").value;

            let data = {
                content,
            };

            axios.post(`/memo/add`, data)
                .then((response) => {
                    memoList.insertBefore(makeNewMemo(response.data.memo), memoList.firstChild);
                });

        };
    }
}