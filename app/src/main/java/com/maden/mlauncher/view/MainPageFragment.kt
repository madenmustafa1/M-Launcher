package com.maden.mlauncher.view


import android.R.attr.bitmap
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maden.mlauncher.R
import com.maden.mlauncher.adapter.AdapterClickListener
import com.maden.mlauncher.adapter.AppsDrawerAdapter
import com.maden.mlauncher.databinding.FragmentMainPageBinding
import com.maden.mlauncher.model.AppInfoModel
import com.maden.mlauncher.util.DateUtil
import com.maden.mlauncher.util.SharedPrefsManager
import com.maden.mlauncher.util.getUserWallpaper


class MainPageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var appInfoModelList: ArrayList<AppInfoModel> = arrayListOf()

    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!

    private var sharedPrefsManager: SharedPrefsManager? = null
    private var setFirstApp: Boolean = false
    private var setSecondApp: Boolean = false
    private var seThirdApp: Boolean = false
    private var firstApp: String? = null
    private var secondApp: String? = null
    private var thirdApp: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefsManager = SharedPrefsManager(requireContext())
        transparentStatusBar()
        setUpApps().also {
            //initButton()
            initApps()
        }
    }

    override fun onResume() {
        super.onResume()
        setUpApps().also {
            initButton()
            initRecyclerView()
        }
    }

    private fun initButton() {
        binding.systemHour.text = DateUtil.getSystemHourAndMin()
        binding.systemDate.text = DateUtil.getSystemDate()
        binding.settingsButton.setOnClickListener {
            Intent(requireActivity(), SettingsActivity::class.java).apply {
                startActivity(this)
            }
        }

        requireContext().getUserWallpaper()?.let {
            val drawable: Drawable = BitmapDrawable(it)
            binding.wallpaper.background = drawable
        }
    }

    private fun initRecyclerView() {
        recyclerView = binding.appDrawerRecylerView
        adapter = AppsDrawerAdapter(appInfoModelList, initAdapterClickListener(), false)
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.let {
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun transparentStatusBar() {
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.transparent)
    }

    private fun setUpApps() {
        val pManager: PackageManager = requireContext().packageManager
        appInfoModelList.clear()
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val allApps = pManager.queryIntentActivities(i, 0)
        for (ri in allApps) {
            if (ri.activityInfo.packageName != "com.maden.mlauncher") {
                val app = AppInfoModel(
                    label = ri.loadLabel(pManager),
                    packageName = ri.activityInfo.packageName,
                    icon = ri.activityInfo.loadIcon(pManager)
                )
                appInfoModelList.add(app)
            }
        }
    }

    private fun initApps() {
        sharedPrefsManager?.let {
            firstApp = it.getString(it.firstApp)
            secondApp = it.getString(it.secondApp)
            thirdApp = it.getString(it.thirdApp)

            for (i in appInfoModelList) {
                firstApp?.let {
                    if (i.packageName == firstApp) setFirstApp(i.icon)
                }
                secondApp?.let {
                    if (i.packageName == secondApp) setSecondApp(i.icon)
                }
                thirdApp?.let {
                    if (i.packageName == thirdApp) setThirdApp(i.icon)
                }
            }
        }

        binding.appFirst.setOnClickListener {
            firstApp?.let {
                goSelectedApp(it)
            } ?: run {
                setFirstApp = true
                shaker()
            }
        }
        binding.appSecond.setOnClickListener {
            secondApp?.let {
                goSelectedApp(it)
            } ?: run {
                setSecondApp = true
                shaker()
            }
        }
        binding.appThird.setOnClickListener {
            thirdApp?.let {
                goSelectedApp(it)
            } ?: run {
                seThirdApp = true
                shaker()
            }
        }

        binding.appFirst.setOnLongClickListener {
            setFirstApp = true
            shaker()
            return@setOnLongClickListener true
        }
        binding.appSecond.setOnLongClickListener {
            setSecondApp = true
            shaker()
            return@setOnLongClickListener true
        }
        binding.appThird.setOnLongClickListener {
            seThirdApp = true
            shaker()
            return@setOnLongClickListener true
        }
    }

    private fun setFirstApp(drawable: Drawable) = binding.appFirst.setImageDrawable(drawable)
    private fun setSecondApp(drawable: Drawable) = binding.appSecond.setImageDrawable(drawable)
    private fun setThirdApp(drawable: Drawable) = binding.appThird.setImageDrawable(drawable)

    private fun goSelectedApp(packageName: String) {
        val launchIntent: Intent? =
            requireContext().packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            requireActivity().startActivity(
                launchIntent,
                null
            )

            requireActivity().overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }

    private fun shaker() {
        adapter = AppsDrawerAdapter(appInfoModelList, initAdapterClickListener(), true)
        recyclerView!!.adapter = adapter
        adapter!!.notifyDataSetChanged()
    }

    private fun initAdapterClickListener(): AdapterClickListener {
        return object : AdapterClickListener {
            override fun onClickListener(packageName: String, index: Int, drawable: Drawable) {
                when {
                    setFirstApp -> {
                        setFirstApp = false
                        firstApp = packageName
                        setFirstApp(drawable)
                        sharedPrefsManager?.setData(sharedPrefsManager?.firstApp ?: "", packageName)
                    }
                    setSecondApp -> {
                        setSecondApp = false
                        secondApp = packageName
                        setSecondApp(drawable)
                        sharedPrefsManager?.setData(sharedPrefsManager?.secondApp ?: "", packageName)
                    }
                    seThirdApp -> {
                        seThirdApp = false
                        thirdApp = packageName
                        setThirdApp(drawable)
                        sharedPrefsManager?.setData(sharedPrefsManager?.thirdApp ?: "", packageName)
                    }
                    else -> {
                        goToSecondApp(packageName)
                    }
                }
            }
        }
    }

    private fun goToSecondApp(packageName: String){
        val launchIntent: Intent? =
            requireContext().packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            ContextCompat.startActivity(
                requireContext(),
                launchIntent,
                null
            )
            requireActivity().overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }
}