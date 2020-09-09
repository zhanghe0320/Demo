package com.demo.mvp;

import com.demo.fragment.base.LazyLoadFragment;

/**
 */
public abstract class LastBaseFragment extends LazyLoadFragment implements BaseView {

    protected boolean isAttachedContext(){
        return getActivity() != null;
    }

    /**
     * 检查activity连接情况
     */
    public void checkActivityAttached() {
        if (getActivity() == null) {
            throw new ActivityNotAttachedException();
        }
    }

    public static class ActivityNotAttachedException extends RuntimeException {
        public ActivityNotAttachedException() {
            super("Fragment has disconnected from Activity ! - -.");
        }
    }

}
