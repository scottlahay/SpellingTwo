package scott.spelling.system

import scott.spelling.model.Grade
import scott.spelling.model.Grades
import scott.spelling.model.Week

class FirebaseStockTheDatabase {

    companion object {

        val G5_week0 = Week("0", "one", "two", "three")
        val G5_week1 = Week("1", "quiet", "aches", "shake", "knocked", "jacket", "quarter", "quickly", "knowing", "quarrel", "speaker", "questions", "kneeling", "earthquake", "mechanic", "orchestra", "knothole", "inquire", "sequence", "require", "character")
        val G5_week2 = Week("2", "concert", "certain", "circus", "couple", "credit", "celebrate", "village", "graduate", "grateful", "coupon", "shortage", "gesture", "generous", "garage", "license", "sausage", "gadget", "regular", "dangerous", "icicles")
        val G5_week3 = Week("3", "frightened", "suffer", "paragraph", "effort", "autographs", "telephone", "dolphins", "enough", "laughter", "symphony", "physical", "photography", "atmosphere", "ﬂawless", "geography", "triumph", "typhoid", "hyphen", "typhoon", "tough")
        val G5_week4 = Week("4", "wreath", "known", "typewriters", "written", "wrapper", "arrow", "correct", "mirror", "surround", "knead", "knotted", "resign", "designed", "assign", "wrinkled", "foreign", "wrestler", "campaign", "cologne", "knuckles")
        val G5_week5 = Week("5", "ﬁnal", "oval", "fuel", "equals", "pickle", "tickle", "double", "jungle", "panel", "towels", "cancel", "plural", "mammal", "sparkled", "whistle", "aisle", "scramble", "channel", "spiral", "jingle")
        val G5_week7 = Week("7", "complain", "braid", "drains", "coast", "toasted", "decay", "roasting", "throwing", "tomorrow", "borrow", "boast", "swallows", "foamy", "poach", "cocoa", "fainted", "scarecrow", "gained", "hoax", "painful")
        val G5_week8 = Week("8", "feather", "sweater", "underneath", "agreement", "meadows", "speech", "needles", "keeping", "treasure", "northeast", "steady", "creature", "breathe", "pleasure", "succeed", "sweeter", "healthy", "preacher", "leather", "wealth")
        val G5_week9 = Week("9", "hawk", "faucet", "author", "pause", "daughter", "withdraw", "hauled", "awfully", "unlawful", "lawyer", "strawberries", "squawk", "saucers", "drawer", "caution", "vault", "naughty", "gnawing", "awkward", "exhaust")
        val G5_week10 = Week("10", "deceived", "received", "brief", "believe", "infield", "review", "freight", "vein", "shield", "pieces", "eighty", "niece", "yield", "either", "neither", "deceit", "grief", "thief", "achieve", "conceive")
        val G5_week11 = Week("11", "crowded", "joined", "voyage", "soiled", "enjoyable", "counted", "powder", "oyster", "poise", "boundary", "loyalty", "moisture", "poisonous", "mountainous", "courageous", "choir", "toiled", "broiled", "avoid", "joint")
        val G5_week13 = Week("13", "transport", "transfer", "increase", "disgrace", "uncovered", "discontinue", "uncooked", "discovered", "include", "inhale", "disturb", "incomplete", "unjust", "transplant", "unusual", "unselfish", "insecure", "distance", "inspire", "inexpensive")
        val G5_week16 = Week("16", "postpone", "postwar", "overlooked", "overboard", "forecast", "forearm", "overreact", "conserve", "conditions", "consoled", "compete", "forewarn", "overcome", "company", "cooperate", "conquer", "foresee", "contribute", "commend", "committee")
        val G5_week17 = Week("17", "trio", "subject", "midwinter", "submit", "midnight", "triangle", "midstream", "triple", "middle", "bicycles", "bisect", "subscribe", "midair", "substitute", "subtract", "biceps", "tripod", "midday", "subdue", "subside")
        val G5_week19 = Week("19", "shoelaces", "paperback", "haircut", "jellyfish", "underwater", "shipwreck", "sunshine", "fingernail", "cornbread", "snowdrift", "hallway", "kneecap", "pancakes", "rainbows", "blue-green", "self-control", "spellbound", "countdown", "grapefruit", "forty-six")
        val G5_week20 = Week("20", "Georgia's", "class's", "America's", "Canada's", "who'll", "champion's", "Japan's", "women's", "parent's", "farmers'", "college's", "you'd", "Kansas'", "Montreal's", "governor's", "senator's", "mayor's", "Mexico's", "they've", "Texas'")
        val G5_week21 = Week("21", "normal", "problem", "shallow", "manners", "symbol", "perform", "suggest", "fossil", "scanner", "expert", "collect", "mental", "forbid", "signal", "command", "cassette", "rescue", "challenge", "partner", "support")
        val G5_week22 = Week("22", "unit", "plastic", "helmet", "mission", "rubble", "splendid", "cover", "dozen", "closet", "divide", "mayor", "modern", "grandparents", "watermelon", "fingerprints", "posture", "silence", "reduce", "answers", "success")
        val G5_week23 = Week("23", "history", "unlikely", "remember", "athletic", "religion", "citizen", "animal", "magazine", "popular", "artistic", "yesterday", "stadium", "horrible", "beautiful", "serious", "vehicle", "opposite", "impression", "electric", "chocolate")
        val G5_week25 = Week("25", "louder", "scientist", "warrior", "director", "scariest", "slightest", "prisoner", "sweeper", "novelist", "greater", "emperor", "busiest", "tourist", "drearier", "interpreter", "hungrier", "counselor", "inspector", "cartoonist", "vocalist")
        val G5_week26 = Week("26", "employee", "mountaineer", "absentee", "excellent", "reliant", "volunteer", "puppeteer", "engineer", "payee", "participant", "resident", "pleasant", "competent", "violent", "assistant", "accountant", "repellent", "abundant", "dominant", "applicant")
        val G5_week27 = Week("27", "shyness", "peaceful", "forward", "cheerful", "afterward", "darken", "plentiful", "loudness", "backward", "tighten", "politeness", "sharpen", "memorize", "wonderful", "recognize", "shameful", "friendliness", "alphabetize", "delightful", "wilderness")
        val G5_week28 = Week("28", "possible", "excitement", "terrible", "neighborhood", "leadership", "equipment", "profitable", "ownership", "falsehood", "championship", "statement", "sensible", "motherhood", "dependable", "comfortable", "visible", "likelihood", "agreeable", "durable", "")
        val G5_week29 = Week("29", "direction", "protection", "allowance", "equality", "selective", "population", "massive", "captive", "humidity", "vacation", "importance", "opinion", "election", "humanity", "objection", "confidence", "imitation", "attendance", "originality", "independence")
        val G5_week31 = Week("31", "shredded", "planned", "pledged", "throbbing", "spinning", "hoping", "decided", "strangest", "shipping", "usable", "valuable", "pleasing", "scraping", "skidded", "imagination", "introducing", "disapproved", "unforgivable", "persuaded", "amazing")
        val G5_week32 = Week("32", "envied", "worrying", "friendlier", "obeying", "sunniest", "readily", "stickiest", "hastily", "heavier", "noisily", "sturdier", "greedily", "sleepiest", "merrily", "occupying", "supplying", "classified", "magnifying", "angrily", "cheerily")
        val G5_week33 = Week("33", "centuries", "groceries", "countries", "journeys", "families", "delays", "kidneys", "decoys", "bakeries", "libraries", "cavities", "cranberries", "mysteries", "activities", "injuries", "apologies", "secretaries", "authorities", "victories", "relays")
        val G5_week34 = Week("34", "moose", "trout", "salmon", "wolves", "calves", "halves", "scarves", "broccoli", "spaghetti", "radios", "tomatoes", "sheriffs", "cuffs", "potatoes", "beliefs", "chiefs", "volcanoes", "species", "igloos", "tornadoes")
        val G5_week35 = Week("35", "yolk", "familiar", "separate", "weird", "February", "column", "surprises", "islands", "misspelled", "ballet", "fashion", "stomach", "recommend", "famous", "prairie", "forfeit", "wisdom", "chosen", "weather", "punctuation")
        val grade_5 = Grade("5", listOf(G5_week0, G5_week1, G5_week2, G5_week3, G5_week4, G5_week5, G5_week7, G5_week8, G5_week9, G5_week10, G5_week11, G5_week13, G5_week16, G5_week17, G5_week19, G5_week20, G5_week21, G5_week22, G5_week23, G5_week25, G5_week26, G5_week27, G5_week28, G5_week29, G5_week31, G5_week32, G5_week33, G5_week34, G5_week35))
      
        val G6_week1 = Week("1", "echoes", "chorus", "chemistry", "qualify", "acknowledge", "remarkable", "locksmith", "quantity", "technical", "banquet", "knowledge", "required", "keyboard", "chrome", "antique", "knelt", "headache", "unique", "schedule", "technique")
        val G6_week2 = Week("2", "crystal", "angle", "engaged", "advantage", "pledges", "carbon", "processed", "medicine", "celebration", "icicles", "language", "budget", "guesses", "refrigerator", "conjugate", "magical", "intelligent", "cartridge", "genuine", "recipe")
        val G6_week3 = Week("3", "photographed", "officer", "triumphant", "afford", "toughen", "fifteen", "prefer", "physician", "fragrant", "pamphlet", "saxophone", "effective", "coffee", "phrase", "hyphenate", "magnificent", "sufficient", "emphasize", "hemisphere", "laughable")
        val G6_week4 = Week("4", "purpose", "composure", "diseases", "casual", "seasonal", "resemble", "measuring", "husband", "position", "visual", "trousers", "instruments", "desirable", "instructor", "leisurely", "deserving", "gymnasium", "version", "treasury", "usually")
        val G6_week5 = Week("5", "insure", "information", "exploration", "ashamed", "partial", "nourish", "social", "brochure", "invention", "assure", "facial", "convention", "official", "machinery", "parachute", "negotiate", "accomplish", "potential", "appreciate", "quotient")
        val G6_week7 = Week("7", "scented", "adolescent", "scattered", "scissors", "scientific", "screaming", "muscles", "scalding", "scenery", "crescent", "descending", "sculpture", "escape", "scampered", "scenic", "miscellaneous", "fascinating", "luscious", "discipline", "conscience")
        val G6_week8 = Week("8", "stretches", "designer", "wristwatch", "fetched", "kitchen", "wreckage", "wrestling", "crutches", "hatchet", "wrath", "unmatched", "cologne", "scratched", "resigned", "sketching", "foreigner", "campaign", "awry", "gnarled", "reigned")
        val G6_week9 = Week("9", "doubt", "knickers", "tombstone", "lightning", "debt", "softener", "almighty", "solemn", "fasten", "whistling", "castles", "column", "hymns", "eyesight", "listening", "plumber", "playwright", "condemn", "moisten", "crumbs")
        val G6_week10 = Week("10", "searching", "appeared", "millionaire", "silverware", "compared", "stairway", "earthenware", "carefully", "barely", "squares", "gears", "earning", "unfairly", "unaware", "earrings", "earnest", "research", "despair", "questionnaire", "rehearsal")
        val G6_week11 = Week("11", "athlete", "proceed", "delete", "extreme", "repeated", "esteem", "reasonable", "revealed", "complete", "greasy", "achievement", "squeezed", "delivery", "trolley", "ecology", "nieces", "concealed", "guarantee", "believable", "succeeded")
        val G6_week13 = Week("13", "borrower", "doughnut", "although", "poultry", "bowling", "fallow", "mistletoe", "yellow", "growth", "approaches", "boulders", "stowaway", "thoroughly", "cocoa", "oboe", "bungalow", "tiptoed", "rowboat", "cantaloupe", "shoulders")
        val G6_week14 = Week("14", "conceit", "sleigh", "height", "veil", "seized", "yields", "weird", "mischief", "neighbourly", "reindeer", "fiercely", "unbelievable", "briefly", "pierced", "diesel", "perceived", "relieved", "protein", "ancient", "retrieve")
        val G6_week15 = Week("15", "brawny", "laundry", "taught", "paused", "autumn", "awning", "awesome", "launched", "astronauts", "squawking", "drawback", "exhausted", "saucepan", "automatically", "dinosaur", "authentic", "withdrawal", "thesaurus", "precautions", "applause")
        val G6_week16 = Week("16", "mildew", "guitar", "gloomy", "cruise", "guilty", "curfew", "pewter", "juicy", "smoother", "bruised", "quilted", "building", "fruitful", "shampoo", "soothing", "suitable", "pursued", "biscuit", "circuit", "nuisance")
        val G6_week17 = Week("17", "essays", "boiled", "oysters", "maintenance", "employment", "embroidery", "crayons", "exploit", "turmoil", "ointment", "strainer", "praised", "disappointment", "container", "remainder", "faithful", "avoided", "rejoicing", "mayonnaise", "acquaintance")
        val G6_week19 = Week("19", "coward", "drowsily", "bough", "endow", "mouthful", "compound", "pronoun", "household", "downstream", "discount", "surrounding", "pronounced", "foundation", "resounding", "lounge", "announce", "account", "counterpart", "outrageous", "drought")
        val G6_week20 = Week("20", "incredible", "immortal", "intolerant", "immigrant", "illogical", "immature", "illegal", "improperly", "incapable", "irregular", "impatient", "impolitely", "indirect", "indefinite", "immaterial", "illiterate", "impractical", "irresponsible", "irrational", "illegible")
        val G6_week22 = Week("22", "forewarning", "postscript", "postwar", "encourage", "overweight", "overflow", "foresight", "embellish", "emblazon", "engrave", "endanger", "foreground", "embitter", "overdue", "foreknowledge", "overprotect", "embankment", "embattle", "enlistment", "enlighten")
        val G6_week23 = Week("23", "antifreeze", "supervise", "semifinal", "antiseptic", "transparent", "substitution", "antidote", "submarine", "superficial", "subscription", "semicircle", "counteract", "counterattack", "semicolon", "supersonic", "transistor", "ultraviolet", "transferred", "counterfeit", "antibodies")
        val G6_week25 = Week("25", "tricolor", "midwestern", "tripled", "university", "midsummer", "biweekly", "unicycle", "monorail", "uniform", "bisects", "biplane", "triangular", "binoculars", "unison", "trilogy", "monotonous", "monosyllable", "universal", "triplicate", "biannual")
        val grade_6 = Grade("6", listOf(G6_week1, G6_week2, G6_week3, G6_week4, G6_week5, G6_week7, G6_week8, G6_week9, G6_week10, G6_week11, G6_week13, G6_week16, G6_week17, G6_week19, G6_week20, G6_week22, G6_week23, G6_week25))

        val grades = Grades(listOf(grade_5, grade_6))
    }
}