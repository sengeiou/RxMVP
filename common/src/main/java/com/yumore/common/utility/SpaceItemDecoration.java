package com.yumore.common.utility;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * item decoration for recycler view
 *
 * @author Nathaniel
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    public static final int LINEAR_LAYOUT = 0;
    public static final int GRID_LAYOUT = 1;
    public static final int STAGGERED_GRID_LAYOUT = 2;
    private int leftRight;
    private int topBottom;
    private final int headItemCount;
    private int space;
    private boolean includeEdge;
    private int spanCount;
    @LayoutManager
    private final int layoutManager;
    private GridLayoutManager gridLayoutManager;

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param leftRight     leftRight
     * @param topBottom     topBottom
     * @param headItemCount headItemCount
     * @param layoutManager layoutManager
     */
    public SpaceItemDecoration(int leftRight, int topBottom, int headItemCount, @LayoutManager int layoutManager) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
        this.headItemCount = headItemCount;
        this.layoutManager = layoutManager;
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param space         space
     * @param includeEdge   includeEdge
     * @param layoutManager layoutManager
     */
    public SpaceItemDecoration(int space, boolean includeEdge, @LayoutManager int layoutManager) {
        this(space, 0, includeEdge, layoutManager);
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param space         space
     * @param headItemCount headItemCount
     * @param includeEdge   includeEdge
     * @param layoutManager layoutManager
     */
    public SpaceItemDecoration(int space, int headItemCount, boolean includeEdge, @LayoutManager int layoutManager) {
        this.space = space;
        this.headItemCount = headItemCount;
        this.includeEdge = includeEdge;
        this.layoutManager = layoutManager;
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param space         space
     * @param headItemCount headItemCount
     * @param layoutManager layoutManager
     */
    public SpaceItemDecoration(int space, int headItemCount, @LayoutManager int layoutManager) {
        this(space, headItemCount, true, layoutManager);
    }

    /**
     * LinearLayoutManager or GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param space         space
     * @param layoutManager layoutManager
     */
    public SpaceItemDecoration(int space, @LayoutManager int layoutManager) {
        this(space, 0, true, layoutManager);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        switch (layoutManager) {
            case LINEAR_LAYOUT:
                setLinearLayoutSpaceItemDecoration(outRect, view, parent, state);
                break;
            case GRID_LAYOUT:
                GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
                //??????
                spanCount = gridLayoutManager.getSpanCount();
                setNGridLayoutSpaceItemDecoration(outRect, view, parent, state);
                break;
            case STAGGERED_GRID_LAYOUT:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
                //??????
                spanCount = staggeredGridLayoutManager.getSpanCount();
                setNGridLayoutSpaceItemDecoration(outRect, view, parent, state);
                break;
            default:
                break;
        }
    }

    /**
     * LinearLayoutManager spacing
     *
     * @param outRect outRect
     * @param view    view
     * @param parent  parent
     * @param state   state
     */
    private void setLinearLayoutSpaceItemDecoration(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     *
     * @param outRect outRect
     * @param view    view
     * @param parent  parent
     * @param state   state
     */
    private void setNGridLayoutSpaceItemDecoration(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view) - headItemCount;
        if (headItemCount != 0 && position == -headItemCount) {
            return;
        }
        int column = position % spanCount;
        if (includeEdge) {
            outRect.left = space - column * space / spanCount;
            outRect.right = (column + 1) * space / spanCount;
            if (position < spanCount) {
                outRect.top = space;
            }
            outRect.bottom = space;
        } else {
            outRect.left = column * space / spanCount;
            outRect.right = space - (column + 1) * space / spanCount;
            if (position >= spanCount) {
                outRect.top = space;
            }
        }

    }

    /**
     * GridLayoutManager????????????????????????????????????????????????????????????????????????
     *
     * @param outRect outRect
     * @param view    view
     * @param parent  parent
     * @param state   state
     */
    private void setGridLayoutSpaceItemDecoration(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //????????????????????????????????????
        gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
        int totalCount = gridLayoutManager.getItemCount();
        int surplusCount = totalCount % gridLayoutManager.getSpanCount();
        int childPosition = parent.getChildAdapterPosition(view);
        //???????????????
        if (gridLayoutManager.getOrientation() == RecyclerView.VERTICAL) {
            if (surplusCount == 0 && childPosition > totalCount - gridLayoutManager.getSpanCount() - 1) {
                //??????????????????bottom
                outRect.bottom = topBottom;
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.bottom = topBottom;
            }
            //????????????????????????
            if ((childPosition + 1 - headItemCount) % gridLayoutManager.getSpanCount() == 0) {
                //???????????????????????????????????????????????????????????????
                //outRect.right = leftRight;
            }
            outRect.top = topBottom;
            outRect.left = leftRight / 2;
            outRect.right = leftRight / 2;
        } else {
            if (surplusCount == 0 && childPosition > totalCount - gridLayoutManager.getSpanCount() - 1) {
                //????????????????????????
                outRect.right = leftRight;
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.right = leftRight;
            }
            //????????????????????????
            if ((childPosition + 1) % gridLayoutManager.getSpanCount() == 0) {
                outRect.bottom = topBottom;
            }
            outRect.top = topBottom;
            outRect.left = leftRight;
        }
    }

    public GridLayoutManager getGridLayoutManager() {
        return gridLayoutManager;
    }

    //?????????LINEAR_LAYOUT,GRID_LAYOUT,STAGGERED_GRID_LAYOUT
    @IntDef({LINEAR_LAYOUT, GRID_LAYOUT, STAGGERED_GRID_LAYOUT})
    //??????????????????????????????,????????????,???????????????. class ??????.
    @Retention(RetentionPolicy.SOURCE)
    public @interface LayoutManager {
        int type() default LINEAR_LAYOUT;
    }
}