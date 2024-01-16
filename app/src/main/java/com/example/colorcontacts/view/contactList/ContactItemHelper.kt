package com.example.colorcontacts.view.contactList

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.net.Uri
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.colorcontacts.data.UserList

/**
 * TODO 스와이프시 통화 기능
 *
 * @param requireContext 프래그먼트에서 받아옴
 *
 * 왼쪽에서 슬라이드 하면서 리사이클러 뷰를 밀면
 * 통화가 가능함
 *
 * 통화 권한을 등록해야함으로 Mainfest에 <uses-permission android:name="android.permission.CALL_PHONE" />를 입력
 */
class ContactItemHelper(val context: Context): ItemTouchHelper.Callback() {

    //드래그 또는 스와이프를 할 수 있는데, 상 하 좌 우로 선택가능, 드래그롤 통해 순서변경도 가능
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFqwelags = ItemTouchHelper.RIGHT
        return makeMovementFlags(0, swipeFqwelags)
        //오른쪽으로만 슬라이드 가능하게 설정
    }

    //드래그시 호출
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // 드래그 기능은 사용하지 않으므로 false 반환
        return false
    }

    //스와이프 동작 활성화
    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }
    //롱터치는 비활성화한다
    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    /**
     * TODO 스와이프 설정
     *
     * @param viewHolder ContactAdapter의 ItemViewHolder
     * @param direction 스와이프의 동작 방향
     * 오른쪽으로 스와이프할시 전화 걸기 인텐트 생성
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val personViewHolder = viewHolder as ContactAdapter.ItemViewHolder
        val phoneNumber = UserList.userList.find { it.name == personViewHolder.name.text}?.phone

        if (direction == ItemTouchHelper.RIGHT){
            val callUriSwiped = Uri.parse("tel:$phoneNumber ")
            val callIntent = Intent(Intent.ACTION_CALL, callUriSwiped)
            context.startActivity(callIntent)
        }
    }

    //레이아웃 뒤에 숨겨진 뷰들을 냅두고 겉에 있는 레이아웃만 이동시킨다
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            val view = (viewHolder as ContactAdapter.ItemViewHolder).swipeLayout //리사이클러뷰의 레이아웃
            getDefaultUIUtil().onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive)
        }//스와이프 동작에 따른 UI 변화를 적용
    }
}