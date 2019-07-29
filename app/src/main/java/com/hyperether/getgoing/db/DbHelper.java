package com.hyperether.getgoing.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.List;


/**
 * Created by Slobodan on 7/11/2017.
 */
public class DbHelper {

    public interface OnDataLoadListener {
        public void onLoad();
    }

    private static final String DATABASE_NAME = "getgoing_db";
    private static DbHelper instance;
    private Handler mHandler;

    private AppDatabase db;

    private DbHelper(Context ctxt) {
        db = Room.databaseBuilder(ctxt, AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
    }

    public static DbHelper getInstance(Context ctxt) {
        if (instance == null)
            instance = new DbHelper(ctxt);
        return instance;
    }

    public AppDatabase getAppDatabase() {
        return db;
    }

    // TODO: add threading here!
    public void insertRoute(final DbRoute dbRoute,
                            final List<DbNode> nodeList) {
        getDbHandler().post(new Runnable() {
            @Override
            public void run() {
                long routeId = db.dbRouteDao().insertRoute(dbRoute);
                DbRoute route = db.dbRouteDao().getRouteById(routeId);

                if (route != null) {
                    for (DbNode currentNode : nodeList) {
                        db.dbNodeDao().insertNode(new DbNode(0, currentNode.getLatitude(), currentNode.getLongitude(),
                                currentNode.getVelocity(), currentNode.getIndex(),
                                routeId));
                    }
                }
            }
        });
    }

    public void populateRoutes(final List<DbRoute> routes, final OnDataLoadListener dataLoadListener) {
        getDbHandler().post(new Runnable() {
            public void run() {
                routes.addAll(db.dbRouteDao().getAll());
                dataLoadListener.onLoad();
            }
        });
    }

    public void getRouteById(final List<DbRoute> routes, final long id) {
        getDbHandler().post(new Runnable() {
            public void run() {
                DbRoute r1 = db.dbRouteDao().getRouteById(id);
                routes.add(r1);
            }
        });
    }

    public void deleteRouteById(final long id) {

        getDbHandler().post(new Runnable() {
            public void run() {
                db.dbRouteDao().deleteRouteById(id);
            }
        });
    }

    public void getAllNodesByRouteId(final List<DbNode> nodes, final long id) {

        getDbHandler().post(new Runnable() {
            public void run() {
                nodes.addAll(db.dbNodeDao().getAllByRouteId(id));
            }
        });
    }

    public void getRouteAndNodesRouteId(final List<DbRoute> routes, final List<DbNode> nodes, final long id, final OnDataLoadListener dataLoadListener) {
        getDbHandler().post(new Runnable() {
            public void run() {
                DbRoute r1 = db.dbRouteDao().getRouteById(id);
                routes.add(r1);
                nodes.addAll(db.dbNodeDao().getAllByRouteId(id));
                dataLoadListener.onLoad();
            }
        });

    }

    public Handler getDbHandler() {
        if (mHandler == null) {
            HandlerThread mThread = new HandlerThread("db-thread");
            mThread.start();
            mHandler = new Handler(mThread.getLooper());
        }
        return mHandler;
    }
}