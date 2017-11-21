## How to run

Start the Play app:

```
sbt run
```

And open [http://localhost:9000/card](http://localhost:9000/card)

___________________________________________________________________________________________________________________________


Postgres database Table insertion queries :

Card_Details Table : To store the credit card information.

                                 Table "public.card_details"
     Column      |         Type          | Modifiers | Storage  | Stats target | Description
-----------------+-----------------------+-----------+----------+--------------+-------------
 id              | bigint                | not null  | plain    |              |
 name            | character varying(50) |           | extended |              |
 state           | character varying(50) |           | extended |              |
 zip             | character varying(20) |           | extended |              |
 account_type    | character varying(10) |           | extended |              |
 card_number     | character varying(50) |           | extended |              |
 expiration_date | character varying(10) |           | extended |              |
 cvv2            | character varying(10) |           | extended |              |
 account_number  | character varying(50) |           | extended |              |
 prepaid         | character varying(10) |           | extended |              |
 amount          | double precision      |           | plain    |              |
Indexes:
    "card_details_pkey" PRIMARY KEY, btree (id)
Referenced by:
    TABLE "transaction" CONSTRAINT "transaction_id_fkey" FOREIGN KEY (id) REFERENCES card_details(id)
	

Query:	
```
create table Card_Details(id bigint not null primary key,name character varying(50),state character varying(50),zip character varying(20),
account_type character varying(10),card_number character varying(50),expiration_date character varying(10), cvv2 character varying(10),
account_number character varying(50),prepaid character varying(10), amount double precision);
```

Transaction Table : To store the transaction details.

                                    Table "public.transaction"
        Column        |         Type          | Modifiers | Storage  | Stats target | Description
----------------------+-----------------------+-----------+----------+--------------+-------------
 trans_id             | character varying(50) | not null  | extended |              |
 authorization_code   | character varying(50) |           | extended |              |
 account_token        | character varying(50) |           | extended |              |
 id                   | bigint                |           | plain    |              |
 primoris_fee         | character varying(50) |           | extended |              |
 installment_total    | character varying(50) |           | extended |              |
 callid               | character varying(50) |           | extended |              |
 settlement_id        | character varying(50) |           | extended |              |
 payment_detail_id    | character varying(50) |           | extended |              |
 payment_date         | character varying(50) |           | extended |              |
 amount               | character varying(50) |           | extended |              |
 payment_token        | character varying(50) |           | extended |              |
 payment_token_status | character varying(50) |           | extended |              |
 payment_type         | character varying(50) |           | extended |              |
 account_status       | character varying(50) |           | extended |              |
 payment_name         | character varying(50) |           | extended |              |
 amount_total         | character varying(50) |           | extended |              |
 installment_made     | character varying(50) |           | extended |              |
 confirmation_number  | character varying(50) |           | extended |              |
 installment_plan     | character varying(50) |           | extended |              |
 add_date             | character varying(50) |           | extended |              |
 retry_id             | character varying(50) |           | extended |              |
Indexes:
    "transaction_pkey" PRIMARY KEY, btree (trans_id)
Foreign-key constraints:
    "transaction_id_fkey" FOREIGN KEY (id) REFERENCES card_details(id)
	
Query:
```
create Table Transaction(trans_id varchar(50) primary key,authorization_code varchar(50),account_token varchar(50),id bigint REFERENCES Card_Details(id),
primoris_fee varchar(50),installment_total varchar(50),callID varchar(50),settlement_id varchar(50),payment_detail_id varchar(50),payment_date varchar(50),
amount varchar(50),payment_token varchar(50),payment_token_status varchar(50),payment_type varchar(50),account_status varchar(50),payment_name varchar(50),
amount_total varchar(50),installment_made varchar(50),confirmation_number varchar(50),installment_plan varchar(50),add_date varchar(50),retry_id varchar(50))
```



