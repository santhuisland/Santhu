package com.island.wiskers;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.island.wiskers.datamodel.bw;

import java.lang.ref.WeakReference;


/**
 * Created by Island on 01/08/15.
 */

public class PhotoAdapter extends PagerAdapter
{
    LayoutInflater mInflater = null;
    Activity mParent = null;
    bw mData = null;
    int mPageWidth = 0;
    int mPageHeight = 0;
    Resources   mResource=null;


    public PhotoAdapter(Activity parent, LayoutInflater inflater,int pPageWidth,int pPageHeight)
    {
        mParent = parent;
        mInflater = inflater;
        mPageWidth = pPageWidth;
        mPageHeight = pPageHeight;
        mResource = AppGlobals.mAppContext.getResources();
    }

    public void loadData(bw pData)
    {
        mData = pData;
        notifyDataSetChanged();
    }

    public Object instantiateItem(ViewGroup container, int position)
    {
        View page = null;

        page = mInflater.inflate(R.layout.iconpage, container, false);
        if(page != null)
        {
            try {
                ImageView lphotoview = (ImageView) page.findViewById(R.id.photoview);
                if (mData != null) {
                    if (mData.getCats().get(position).getImage().getHdpi() != null)
                    {
                        if(mData.getCats().get(position).getImage().getHdpi().equalsIgnoreCase(mResource.getResourceEntryName(R.drawable.bengal)))
                            loadBitmap(R.drawable.bengal, lphotoview);
                        else if(mData.getCats().get(position).getImage().getHdpi().equalsIgnoreCase(mResource.getResourceEntryName(R.drawable.bombay)))
                            loadBitmap(R.drawable.bombay, lphotoview);
                        else if(mData.getCats().get(position).getImage().getHdpi().equalsIgnoreCase(mResource.getResourceEntryName(R.drawable.burmese)))
                            loadBitmap(R.drawable.burmese, lphotoview);
                        else if(mData.getCats().get(position).getImage().getHdpi().equalsIgnoreCase(mResource.getResourceEntryName(R.drawable.exotic_shorthair)))
                            loadBitmap(R.drawable.exotic_shorthair, lphotoview);
                        else if(mData.getCats().get(position).getImage().getHdpi().equalsIgnoreCase(mResource.getResourceEntryName(R.drawable.siamese)))
                            loadBitmap(R.drawable.siamese, lphotoview);
                        else if(mData.getCats().get(position).getImage().getHdpi().equalsIgnoreCase(mResource.getResourceEntryName(R.drawable.snowshoe)))
                            loadBitmap(R.drawable.snowshoe, lphotoview);
                        else if(mData.getCats().get(position).getImage().getHdpi().equalsIgnoreCase(mResource.getResourceEntryName(R.drawable.turkish_van)))
                            loadBitmap(R.drawable.turkish_van, lphotoview);
                        else
                            loadBitmap(R.drawable.ic_launche, lphotoview);
                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        container.addView(page);

        return (page);
    }


    private void loadBitmap(int resId, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView,mPageWidth,mPageHeight);
        task.execute(resId);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View) object);
    }

    @Override
    public int getCount()
    {
        if(mData != null && mData.getCats() != null)
            return mData.getCats().size();
        else
            return 0;
    }

    @Override
    public float getPageWidth(int position)
    {

        return (1f);
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {

        return (view == object);
    }


    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int mResID = 0;
        private int mWidth = 0;
        private int mHeight = 0;

        public BitmapWorkerTask(ImageView imageView,int pWidth,int pHeight) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
            mHeight = pHeight;
            mWidth = pWidth;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            mResID = params[0];
            return decodeSampledBitmapFromResource(mResource, mResID, mWidth, mHeight);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }


        private  int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight)
        {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                // Calculate ratios of height and width to requested height and width
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);

                // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
                // with both dimensions larger than or equal to the requested height and width.
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
                inSampleSize = 2;
                // This offers some additional logic in case the image has a strange
                // aspect ratio. For example, a panorama may have a much larger
                // width than height. In these cases the total pixels might still
                // end up being too large to fit comfortably in memory, so we should
                // be more aggressive with sample down the image (=larger inSampleSize).

                final float totalPixels = width * height;

                // Anything more than 2x the requested pixels we'll sample down further
                final float totalReqPixelsCap = reqWidth * reqHeight * 2;

                while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                    inSampleSize++;
                }
            }
            return inSampleSize;
        }


        private Bitmap decodeSampledBitmapFromResource(Resources res, int resId,int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }
    }

}