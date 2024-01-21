package hr.gregl.goldenhourphotography.adapter


import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.gregl.goldenhourphotography.R
import hr.gregl.goldenhourphotography.DATA_PROVIDER_CONTENT_URI
import hr.gregl.goldenhourphotography.model.Item


class ItemAdapter(
    private val context: Context,
    private val items: MutableList<Item>
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private var detailsVisibilityMap = mutableMapOf<Long, Boolean>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val tvGoldenHour = itemView.findViewById<TextView>(R.id.tvGoldenHour)
        private val additionalDetailsLayout =
            itemView.findViewById<LinearLayout>(R.id.additionalDetailsLayout)
        private val tvTimeZone = itemView.findViewById<TextView>(R.id.tvTimeZone)
        private val tvSunrise = itemView.findViewById<TextView>(R.id.tvSunrise)
        private val tvSunset = itemView.findViewById<TextView>(R.id.tvSunset)
        private val tvFirstLight = itemView.findViewById<TextView>(R.id.tvFirstLight)
        private val tvLastLight = itemView.findViewById<TextView>(R.id.tvLastLight)
        private val tvDawn = itemView.findViewById<TextView>(R.id.tvDawn)
        private val tvDusk = itemView.findViewById<TextView>(R.id.tvDusk)
        private val tvWeatherWidget = itemView.findViewById<TextView>(R.id.tvWeatherWidget)
        private val ivWeatherIcon = itemView.findViewById<ImageView>(R.id.ivWeatherIcon)


        @SuppressLint("SetTextI18n")
        fun bind(item: Item, isDetailsVisible: Boolean, context: Context) {
            tvDate.text = "Date: ${item.date}"
            tvGoldenHour.text = "Golden hour: ${item.goldenHour}"
            additionalDetailsLayout.visibility = if (isDetailsVisible) View.VISIBLE else View.GONE
            tvTimeZone.text = "Time zone: ${item.timezone.substringAfter("/")}"
            tvSunrise.text = "Sunrise: ${item.sunrise}"
            tvSunset.text = "Sunset: ${item.sunset}"
            tvFirstLight.text = "First light: ${item.firstLight}"
            tvLastLight.text = "Last light: ${item.lastLight}"
            tvDawn.text = "Dawn: ${item.dawn}"
            tvDusk.text = "Dusk: ${item.dusk}"
            tvWeatherWidget.text = "Temperature: ${item.temperature}°C"

            val iconPath = item.weatherIconPath
            if (iconPath != null) {
                val iconResId = context.resources.getIdentifier(
                    iconPath, null, context.packageName
                )
                if (iconResId != 0) { // Resource exists
                    ivWeatherIcon.setImageResource(iconResId)
                } else { // Default icon if resource not found
                    ivWeatherIcon.setImageResource(R.drawable.weather_empty)
                }
            } else {
                ivWeatherIcon.setImageResource(R.drawable.weather_empty)
            }

            itemView.setOnClickListener {
                additionalDetailsLayout.visibility =
                    if (additionalDetailsLayout.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }

        fun isDetailsVisible(): Boolean {
            return additionalDetailsLayout.visibility == View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false))
    }


    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        // Get the current visibility state for this item, defaulting to false
        val isDetailsVisible = detailsVisibilityMap[item._id] ?: false

        holder.itemView.setOnLongClickListener {
            // Remove item from database
            item._id?.let { id ->
                val uri = ContentUris.withAppendedId(DATA_PROVIDER_CONTENT_URI, id)
                context.contentResolver.delete(uri, null, null)
            }

            // Remove item from the list and notify the adapter
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size - position)
            true
        }
        holder.bind(item, isDetailsVisible, context)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val item = items.getOrNull(holder.adapterPosition)
        item?._id?.let {
            detailsVisibilityMap[it] = holder.isDetailsVisible()
        }
    }
}
