# RxMVP
    rx and mvp frame

## version 1.0.1
    model 全部采用小写
    添加动态权限申请逻辑
    在子fragment和activity使用
    
    @Override
    protected void onResume() {
        super.onResume();
        requestPermission(REQUEST_LOCATION_CODE, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, getString(R.string.rationale_location), new PermissionCallBack() {
            @Override
            public void onPermissionGrantedM(int requestCode, @NonNull String... permissions) {
                LocationUtils.getLocation(getContext());
            }
    
            @Override
            public void onPermissionDeniedM(int requestCode, @NonNull String... permissions) {
                onPermissionDenied(requestCode, permissions);
            }
        });
    }
    
    public void onPermissionDenied(int requestCode, String... permissions) {
        showMessage("onPermissionDenied:" + requestCode + ":" + permissions.length);
    }
