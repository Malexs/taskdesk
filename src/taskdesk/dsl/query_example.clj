(ns taskdesk.dsl.query-example)

; Small exampe of usage

;(select
;  (fields {:name :u.name, :email :email, :a :t.author, :stat :st.id})
;   ;or [:name :email] /// [(count :*)]
;  (from :u :users)
;  (join :t :tasks (== :u.id :t.author))
;  (join :st :stats (== :t.status :st.name))
;  (where ['= :u.name "admin"])
;  )


;Just SQL:
;(jdbc/query db/db-map
;            ["SELECT t.id,
;                                u.name AS author,
;                                date,
;                                title,
;                                description,
;                                milestone,
;                                us.name AS assignee,
;                                t.status,
;                                tg.name AS \"group\"
;                         FROM tasks as t
;                         JOIN users as u
;                            ON u.id = t.author
;                         JOIN users as us
;                            ON us.id = t.assignee
;                         JOIN taskgroups as tg
;                            ON tg.id = t.group
;                         WHERE t.id=?" id])

;The same in DSL:
;(fetch (select (fields {:author :u.name
;                        :date :date
;                        :title :title
;                        :description :description
;                        :milestone :milestone
;                        :assignee :us.name
;                        :status :t.status
;                        :group :tg.name})
;               (from :t :tasks)
;               (join :u :users (== :u.id :t.author))
;               (join :us :users (== :us.id :t.assignee))
;               (join :tg :taskgroups (== :tg.id :t.group))
;               (where (== :t.id id))))
