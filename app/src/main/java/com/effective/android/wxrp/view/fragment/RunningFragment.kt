package com.effective.android.wxrp.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.effective.android.wxrp.R
import com.effective.android.wxrp.RpApplication
import com.effective.android.wxrp.data.sp.LocalizationHelper
import com.effective.android.wxrp.view.activity.MainActivity
import com.effective.android.wxrp.view.activity.SettingActivity
import com.effective.android.wxrp.view.fragment.base.BaseFragment
import com.effective.android.wxrp.view.fragment.base.OnFragmentVisibilityChangeListener
import com.effective.android.wxrp.vm.MainVm
import kotlinx.android.synthetic.main.fragment_result.*

/**
 * 运行结果页面
 * created by yummylau on 2020/01/07
 */
class RunningFragment : BaseFragment() {

    lateinit var vm: MainVm

    override fun getLayoutRes(): Int = R.layout.fragment_result

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProviders.of(activity as MainActivity, MainVm.factory(RpApplication.repository())).get(MainVm::class.java)
        initView()
        addOnVisibilityChangedListener(object : OnFragmentVisibilityChangeListener {
            override fun onFragmentVisibilityChanged(visible: Boolean) {
                if (visible) {
                    vm.checkAllStep()
                    vm.loadPacketList()
                }
            }
        })
        vm.loadPacketList()
    }

    private fun initView() {
        nick.text = LocalizationHelper.getConfigName()
        avatar.background = LocalizationHelper.getConfigAvatar()
        status.text = context?.getString(R.string.running_to_stop)
        switcher.isSelected = true

        switcher.setOnClickListener {
            if (switcher.isSelected) {
                LocalizationHelper.supportPlugin(false)
                status.text = context?.getString(R.string.running_to_start)
                switcher.isSelected = false
            } else {
                status.text = context?.getString(R.string.running_to_stop)
                switcher.isSelected = true
                LocalizationHelper.supportPlugin(true)
            }
        }

        setting.setOnClickListener {
            startActivity(Intent(context, SettingActivity::class.java))
        }
        vm.getPacketData().observe(this, Observer { value ->
            value?.let {
                packet_list.setPackets(it)
            }
        })
    }
}
