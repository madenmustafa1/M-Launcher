package com.maden.mlauncher.adapter

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.maden.mlauncher.R
import com.maden.mlauncher.model.AppInfoModel


class AppsDrawerAdapter(private val context: Context) :
    RecyclerView.Adapter<AppsDrawerAdapter.ViewHolder>(

    ) {
    private var appsList: MutableList<AppInfoModel>? = null

    init {
        setUpApps()
    }

    private fun setUpApps() {
        val pManager: PackageManager = context.packageManager
        appsList = ArrayList()
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val allApps = pManager.queryIntentActivities(i, 0)
        for (ri in allApps) {
            val app = AppInfoModel(
                label = ri.loadLabel(pManager),
                packageName = ri.activityInfo.packageName,
                icon = ri.activityInfo.loadIcon(pManager)
            )
            appsList!!.add(app)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View =
            inflater.inflate(R.layout.item_row_view_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appLabel = appsList!![position].label.toString()
        val appPackage = appsList!![position].packageName.toString()
        val appIcon = appsList!![position].icon
        val textView = holder.textView
        textView.text = appLabel
        val imageView: ImageView = holder.img
        imageView.setImageDrawable(appIcon)

        checkLastItem(holder, position)

        holder.itemView.setOnClickListener {
            val launchIntent: Intent? =
                holder.itemView.context.packageManager.getLaunchIntentForPackage(appPackage)
            if (launchIntent != null) {
                startActivity(
                    holder.itemView.context,
                    launchIntent,
                    null
                ) //null pointer check in case package name was not found
            }
        }
    }


    override fun getItemCount(): Int {
        return appsList!!.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.tv_app_name)
        var img: ImageView = itemView.findViewById(R.id.app_icon)
    }

    private fun checkLastItem(holder: ViewHolder, position: Int){
        val hasMenuKey: Boolean = ViewConfiguration.get(context).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)

        if (position == appsList!!.lastIndex){
            if (hasBackKey && hasMenuKey) {
                // no navigation bar, unless it is enabled in the settings
            } else {
                val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
                params.bottomMargin = 140
                holder.itemView.layoutParams = params
            }
        }else{
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 0
            holder.itemView.layoutParams = params
        }
    }
}