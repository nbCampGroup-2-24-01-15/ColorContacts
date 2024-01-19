package com.example.colorcontacts.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.colorcontacts.databinding.ItemSpinnerBinding

/**
 *  TODO : Custom Spinner 를 구현하기 위해 생성한 어뎁터 클래스
 */
class EventAdapter(context: Context, private val items: Array<String>) :
    ArrayAdapter<String>(context, 0, items) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var binding : ItemSpinnerBinding = ItemSpinnerBinding.inflate(inflater)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View = if (convertView == null) {
            // 새로운 뷰를 생성하고 바인딩을 설정
            binding = ItemSpinnerBinding.inflate(inflater, parent, false)
            binding.root.tag = binding // 바인딩을 뷰의 태그로 설정하여 재사용 가능하도록 함
            binding.root

        } else {
            // 기존의 뷰를 재사용하고 기존의 바인딩을 가져옴
            binding = convertView.tag as ItemSpinnerBinding
            binding.root
        }
        // 여기서 스피너 아이템의 데이터를 가져와서 설정
        val item = getItem(position)
        binding.tvItemSpinner.text = item

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemSpinnerBinding

        val view: View = if (convertView == null) {
            // 새로운 뷰를 생성하고 바인딩을 설정
            binding = ItemSpinnerBinding.inflate(inflater, parent, false)
            binding.root.tag = binding
            binding.tvItemSpinner.text = getItem(position)
            binding.ivItemSpinner.visibility = if (position == 0) View.VISIBLE else View.GONE
            binding.root
        } else {
            // 기존의 뷰를 재사용하고 기존의 바인딩을 가져옴
            binding = convertView.tag as ItemSpinnerBinding
            binding.tvItemSpinner.text = getItem(position)
            binding.ivItemSpinner.visibility = if (position == 0) View.VISIBLE else View.GONE
            binding.root
        }

        return view
    }

}