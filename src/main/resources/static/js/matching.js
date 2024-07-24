// 카테고리에 따른 소카 내용이 달라짐
const selectbox = document.querySelector('.category')
const dollbom = document.querySelector('.category-dollbom')
const sport = document.querySelector('.category-sport')
const study = document.querySelector('.category-study')
const entertainment = document.querySelector('.category-entertainment')
// 시간을 막기 위함
const date = document.querySelector('.date')
const time = document.querySelector('.time')
const today = new Date()

selectbox.addEventListener('change', () => {
    const category = selectbox.options[selectbox.selectedIndex].value

    if(category === '돌봄'){
       dollbom.style.display="flex";
       sport.style.display="none";
       study.style.display="none";
       entertainment.style.display="none";
    }

    else if(category === '교육'){
        dollbom.style.display="none";
        sport.style.display="none";
        study.style.display="flex";
        entertainment.style.display="none";
    }

    else if(category === '운동'){
        dollbom.style.display="none";
        sport.style.display="flex";
        study.style.display="none";
        entertainment.style.display="none";
    }

    else if(category === '예능'){
        dollbom.style.display="none";
        sport.style.display="none";
        study.style.display="none";
        entertainment.style.display="flex";
    }
});

// 전문가 추천해주는 div
const pro_search = document.querySelector('.pro-search');
const random_pro_container = document.getElementById('random_pro_container');
const pro_reset = document.querySelector('.reset')

pro_search.addEventListener("click", () => {
    // 당일 날짜라면 막기
    // || date.value < today.toISOString().substring(0, 10) 전날은 이거 추가하기
    if (today.toISOString().substring(0, 10) === date.value) {
        // alert창 말고 밑에 트렌드에 맞게 바꿔보기
        alert('해당 날짜는 안됩니다.');
    }
})

// 검색버튼을 눌렀을 때 비동기로 sql문 실행하기
// 현재 로그인된 아이디도 가져와야함
function autoSubmit() {

    const category = selectbox.options[selectbox.selectedIndex].value;

    let categoryData;
    if (category === '돌봄') {
        categoryData = dollbom.options[dollbom.selectedIndex].value;
    } else if (category === '운동') {
        categoryData = sport.options[sport.selectedIndex].value;
    } else if (category === '교육') {
        categoryData = study.options[study.selectedIndex].value;
    } else if (category === '예능') {
        categoryData = entertainment.options[entertainment.selectedIndex].value;
    }

    const selectedDate = date.value;
    const selectedTime = time.options[time.selectedIndex].value;

    // console.log(category)
    // console.log(categoryData)
    console.log(selectedDate)
    console.log(selectedTime)


    $.ajax({
        method: 'post',
        url: "/match/submit",
        contentType: 'application/json',
        data: JSON.stringify({
            category: category,
            categoryData: categoryData,
            selectedDate: selectedDate,
            selectedTime: selectedTime
        }),
        success: function (data) {
            console.log('요청성공'); // 서버에서 받은 데이터 출력
            console.log(data)
            console.log(data.length)

            const randomProContainer = $('#random_pro_container');
            randomProContainer.empty();

            // 랜덤 돌려서 딱 한개만 나오게 하기
            const randomIndex = Math.floor(Math.random() * data.length);
            const randomMatch = data[randomIndex];
            console.log(randomMatch);
            
            // 만약, 데이터가 없다면
            if (data.length === 0) {
                randomProContainer.append('<p style="text-align: center; margin-bottom: 50px">검색한 정보에 대한 수업이 존재하지 않습니다.</p>');
            }
            else{
                const html = `
                <div class="random_pro">
                    <input type="hidden" value="${randomMatch.proNumber}">
                    <input type="hidden" value="${randomMatch.classNumber}">
                    <img src="../images/${randomMatch.proProfileImageUrl}">
                    <div>
                        <p>${randomMatch.proName} (${randomMatch.reviewCount})</p>
                        <p>${randomMatch.className} (${randomMatch.classCategoryBig})</p>
                        <p>${randomMatch.classIntro}</p>
                        <p>${randomMatch.proAddress}</p>
                        <p>${randomMatch.classRegisterDate}</p>
                    </div>
                    <!-- 근님이 수정해줘야하는 부분  -->
                    <p><a href="../../templates/html/parent/studyDetail.html">수업 상세보기</a></p>
                </div>
            `
                    randomProContainer.append(html);
            }

            // ====================== 이근 추가한 부분 ====================== //
            const ramdomPro = document.querySelector('.random_pro');
            const toStudyDetailPage = document.querySelector('.random_pro a');

            toStudyDetailPage.addEventListener("click", e=>{
                e.preventDefault(); // a태그 기본동작(페이지 이동) 중지
                let proNumber = ramdomPro.children[0].value;
                let classNumber = ramdomPro.children[1].value;
                const url = '/class/detail' + '?classNumber=' + classNumber + '&proNumber=' + proNumber;
                console.log(url);
                window.location.href = url;

            });
            // ====================== 이근 추가한 부분 ====================== //

            },
        error: function (xhr, status, error) {
            console.error(xhr.responseText); // 오류 메시지 출력
            alert('제대로 수행되지 않았습니다. 다시 시도해주세요.');
        }
    });
}

// // Assume randomProContainer is already defined
// randomProContainer.innerHTML = html;
//
// // Find the anchor tag within the inserted HTML
// const anchorTag = randomProContainer.querySelector('.random_pro a');
//
// // Add a click event listener to the anchor tag
// anchorTag.addEventListener('click', function(event) {
//     event.preventDefault(); // Prevent default link behavior (e.g., page reload)
//
//     // Get proNumber and classNumber from the hidden inputs in the parent div
//     const proNumber = this.closest('.random_pro').querySelector('input[type="hidden"][value="${randomMatch.proNumber}"]').value;
//     const classNumber = this.closest('.random_pro').querySelector('input[type="hidden"][value="${randomMatch.classNumber}"]').value;
//
//     // Construct the URL with parameters
//     const url = '/match/submit/detail' + '?proNumber=' + proNumber + '&classNumber=' + classNumber;
//
//     // Navigate to the new page
//     window.location.href = url;
// });



