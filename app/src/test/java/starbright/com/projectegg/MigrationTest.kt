/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 6 - 9 - 2020.
 */

package starbright.com.projectegg

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import starbright.com.projectegg.dagger.module.StorageModule.Companion.MIGRATION_1_2
import starbright.com.projectegg.data.local.database.ApplicationDatabase
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MigrationTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val migrationHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        ApplicationDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrationFrom1To2_containsCorrectData() {
        val db: SupportSQLiteDatabase = migrationHelper.createDatabase("database", 1)
        db.close()
        migrationHelper.runMigrationsAndValidate(
            "database", 2, true, MIGRATION_1_2
        )
    }
}