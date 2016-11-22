/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 *
 */
public class InMemoryDemoExpenseManager extends ExpenseManager {

    public InMemoryDemoExpenseManager() {
        setup();
    }

    @Override
    public void setup() {
        /*** Begin generating dummy data for In-Memory implementation ***/

        TransactionDAO inMemoryTransactionDAO = new InMemoryTransactionDAO();
        setTransactionsDAO(inMemoryTransactionDAO);

        AccountDAO inMemoryAccountDAO = new InMemoryAccountDAO();
        setAccountsDAO(inMemoryAccountDAO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/
    }

    /**
     * Created by ramA on 11/20/16.
     */

    public static class PersistentExpenseManager extends ExpenseManager {
        private Context ctx;
        public PersistentExpenseManager(Context ctx){
            //Point the constructor to the setup function or our expense manager doesnt
            //get initialized
            this.ctx = ctx;
            setup();
        }
        @Override
        public void setup(){
            //First open an existing database or create new one
            //IMPORTANT DATABASE NAME SHOULD BE YOUR INDEX NUMBER
            SQLiteDatabase mydatabase = ctx.openOrCreateDatabase("140344", ctx.MODE_PRIVATE, null);

            //If it's the first time, we have to create the databases.
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Account(" +
                    "Account_no VARCHAR PRIMARY KEY," +
                    "Bank VARCHAR," +
                    "Holder VARCHAR," +
                    "Initial_amt REAL" +
                    " );");

            //DONOT create a database called Transaction
            //It is a reserved keyword and will give errors in queries
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS TransactionLog(" +
                    "Transaction_id INTEGER PRIMARY KEY," +
                    "Account_no VARCHAR," +
                    "Type INT," +
                    "Amt REAL," +
                    "Log_date DATE," +
                    "FOREIGN KEY (Account_no) REFERENCES Account(Account_no)" +
                    ");");



            //These two functions will hold our DAO instances in memory till the program exists
            PersistentAccountDAO accountDAO = new PersistentAccountDAO(mydatabase);
            //accountDAO.addAccount(new Account("Account12","Sampath bank","Manujith",500));

            setAccountsDAO(accountDAO);

            setTransactionsDAO(new PersistentTransactionDAO(mydatabase));



        }

    }
}
