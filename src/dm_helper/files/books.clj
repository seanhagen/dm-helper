(ns dm-helper.files.books
  (:gen-class)
  (:require
   [dm-helper.files.utils :as util]))

(defn load-all-spells
  ""
  [base-path]
  (util/load-all-things (util/files-from-dir base-path "Bestiary")))

(defn load-all-monsters
  [base-path]
  (util/load-all-things (util/files-from-dir base-path "Spells")))

(defn load-all-items
  [base-path]
  (util/load-all-things (util/files-from-dir base-path "Items")))

(defn load-all-unearthed
  [base-path]
  (util/load-all-things (util/files-from-dir base-path "Unearthed Arcana")))

(defn load-all-from-character-files
  [base-path t]
  (util/load-all-things (util/specific-file-from-dir base-path "Character Files" t))
  )

(defn load-all-feats
  [base-path]
  (load-all-from-character-files base-path "Feats"))

(defn load-all-races
  [base-path]
  (load-all-from-character-files base-path "Races"))

(defn load-all-backgrounds
  [base-path]
  (load-all-from-character-files base-path "Backgrounds"))

;; (defn argh
;;   [content]

;;   (map
;;    #(let [parts (group-by :tag (:content %))]
;;       {:name (:content (first (:name parts)))
;;        :text ""
;;        :optional false
;;        :modifier {}}
;;       )
;;    content)
;;   )

;; (defn parse-autolevel
;;   [al]


;;   {(get-in al [:attrs :level]) (argh al)}

;;   )

(defn fix-autolevels
  [als]

  (println "autolevels: " als)
  ;; {:autolevels (into {} (parse-autolevel (first als)))}

  (group-by #(get-in % [:attrs :level]) als)
  )

(defn load-all-classes
  [base-path]
  (let [files  (util/specific-file-from-dir base-path "Character Files" "Class")
        xml    (util/zip-str
                (slurp (util/data-file (.getPath (first files)))))
        parts  (group-by :tag (:content (first xml)))
        spells (into [] (map #(into {} %) (map #(map util/needs-better-name (util/group-by-tags %)) (:spell parts))))]


    (loop [classes (:class parts)
           results []]
      (let [class (group-by :tag (:content (first classes)))
            autolevel (:autolevel class)
            class (into {} (map util/needs-better-name (dissoc class :autolevel)))
            results (conj results (into class (fix-autolevels autolevel)))]

        (if (> (count classes) 0)
          (recur (rest classes) results)
          {:spells spells
           :classes results}
          )
        )
      )

    ))


(def al [
         {:tag :autolevel, :attrs {:level 1},
          :content [
                    {:tag :feature, :attrs nil,
                     :content [{:tag :name, :attrs nil, :content ["Starting Proficiencies"]}
                               {:tag :text, :attrs nil, :content ["You are proficient with the following items, in addition to any proficiencies provided by your race or background."]}
                               {:tag :text, :attrs nil, :content ["Armor: light armor, medium armor, shields"]}
                               {:tag :text, :attrs nil, :content ["Weapons: simple weapons, martial weapons"]}
                               {:tag :text, :attrs nil, :content ["Tools: none"]}
                               {:tag :text, :attrs nil, :content ["Skills: Choose two from Animal Handling, Athletics, Intimidation, Nature, Perception, and Survival"]}]}
                    {:tag :feature, :attrs nil,
                     :content [{:tag :name, :attrs nil, :content ["Starting Equipment"]}
                               {:tag :text, :attrs nil, :content ["You start with the following items, plus anything provided by your background."]}
                               {:tag :text, :attrs nil, :content nil}
                               {:tag :text, :attrs nil, :content ["• (a) a greataxe or (b) any martial melee weapon"]}
                               {:tag :text, :attrs nil, :content ["• (a) two handaxes or (b) any simple weapon"]}
                               {:tag :text, :attrs nil, :content ["• An explorer's pack, and four javelins"]}
                               {:tag :text, :attrs nil, :content nil}
                               {:tag :text, :attrs nil, :content ["Alternatively, you may start with 2d4 x 10 gp to buy your own equipment."]}]}]}
         {:tag :autolevel, :attrs {:level 1},
          :content [
                    {:tag :feature, :attrs nil,
                     :content [{:tag :name, :attrs nil, :content ["Rage"]}
                               {:tag :text, :attrs nil, :content ["In battle, you fight with primal ferocity. On your turn, you can enter a rage as a bonus action."]}
                               {:tag :text, :attrs nil, :content ["    While raging, you gain the following benefits if you aren't wearing heavy armor:"]}
                               {:tag :text, :attrs nil, :content nil}
                               {:tag :text, :attrs nil, :content ["• You have advantage on Strength checks and Strength saving throws."]}
                               {:tag :text, :attrs nil, :content ["• When you make a melee weapon attack using Strength, you gain a +2 bonus to the damage roll. This bonus increases as you level."]}
                               {:tag :text, :attrs nil, :content ["• You have resistance to bludgeoning, piercing, and slashing damage."]}
                               {:tag :text, :attrs nil, :content nil}
                               {:tag :text, :attrs nil, :content ["If you are able to cast spells, you can't cast them or concentrate on them while raging."]}
                               {:tag :text, :attrs nil, :content ["    Your rage lasts for 1 minute. It ends early if you are knocked unconscious or if your turn ends and you haven't attacked a hostile creature since your last turn or taken damage since then. You can also end your rage on your turn as a bonus action."]}
                               {:tag :text, :attrs nil, :content ["    Once you have raged the maximum number of times for your barbarian level, you must finish a long rest before you can rage again.  You may rage 2 times at 1st level, 3 at 3rd, 4 at 6th, 5 at 12th, and 6 at 17th."]}]}
                    {:tag :feature, :attrs nil, :content [{:tag :name, :attrs nil, :content ["Unarmored Defense"]}
                                                          {:tag :text, :attrs nil, :content ["While you are not wearing any armor, your Armor Class equals 10 + your Dexterity modifier + your Constitution modifier. You can use a shield and still gain this benefit."]}]}]}])
