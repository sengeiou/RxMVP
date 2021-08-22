package com.yumore.utility.greendao;

import com.yumore.utility.module.FileEntity;
import com.yumore.utility.module.PackageEntity;
import com.yumore.utility.module.SummaryEntity;
import com.yumore.utility.module.TaskEntity;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 *
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig fileEntityDaoConfig;
    private final DaoConfig packageEntityDaoConfig;
    private final DaoConfig summaryEntityDaoConfig;
    private final DaoConfig taskEntityDaoConfig;

    private final FileEntityDao fileEntityDao;
    private final PackageEntityDao packageEntityDao;
    private final SummaryEntityDao summaryEntityDao;
    private final TaskEntityDao taskEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
        daoConfigMap) {
        super(db);

        fileEntityDaoConfig = daoConfigMap.get(FileEntityDao.class).clone();
        fileEntityDaoConfig.initIdentityScope(type);

        packageEntityDaoConfig = daoConfigMap.get(PackageEntityDao.class).clone();
        packageEntityDaoConfig.initIdentityScope(type);

        summaryEntityDaoConfig = daoConfigMap.get(SummaryEntityDao.class).clone();
        summaryEntityDaoConfig.initIdentityScope(type);

        taskEntityDaoConfig = daoConfigMap.get(TaskEntityDao.class).clone();
        taskEntityDaoConfig.initIdentityScope(type);

        fileEntityDao = new FileEntityDao(fileEntityDaoConfig, this);
        packageEntityDao = new PackageEntityDao(packageEntityDaoConfig, this);
        summaryEntityDao = new SummaryEntityDao(summaryEntityDaoConfig, this);
        taskEntityDao = new TaskEntityDao(taskEntityDaoConfig, this);

        registerDao(FileEntity.class, fileEntityDao);
        registerDao(PackageEntity.class, packageEntityDao);
        registerDao(SummaryEntity.class, summaryEntityDao);
        registerDao(TaskEntity.class, taskEntityDao);
    }

    public void clear() {
        fileEntityDaoConfig.clearIdentityScope();
        packageEntityDaoConfig.clearIdentityScope();
        summaryEntityDaoConfig.clearIdentityScope();
        taskEntityDaoConfig.clearIdentityScope();
    }

    public FileEntityDao getFileEntityDao() {
        return fileEntityDao;
    }

    public PackageEntityDao getPackageEntityDao() {
        return packageEntityDao;
    }

    public SummaryEntityDao getSummaryEntityDao() {
        return summaryEntityDao;
    }

    public TaskEntityDao getTaskEntityDao() {
        return taskEntityDao;
    }

}