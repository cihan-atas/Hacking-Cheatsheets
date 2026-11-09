<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕸️ InQL - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██╗███╗   ██╗ ██████╗ ██╗     
  ██║████╗  ██║██╔═══██╗██║     
  ██║██╔██╗ ██║██║   ██║██║     
  ██║██║╚██╗██║██║▄▄ ██║██║     
  ██║██║ ╚████║╚██████╔╝███████╗
  ╚═╝╚═╝  ╚═══╝ ╚══▀▀═╝ ╚══════╝
      Comprehensive GraphQL Security Assessment & Introspection Engine
```
<p align="center">
  <img src="https://img.shields.io/badge/InQL-GraphQL_Security-blue?style=for-the-badge" alt="InQL">
  <img src="https://img.shields.io/badge/Author-Doyensec-orange?style=for-the-badge" alt="Doyensec">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Ασφάλεια Ιστού">
  <img src="https://img.shields.io/badge/License-BSD--3--Clause-purple?style=for-the-badge" alt="BSD">
</p>

<p align="center">
  <b>🕸️ Προηγμένο πλαίσιο ασφαλείας CLI και επέκταση Burp Suite που αναπτύχθηκε από την Doyensec για την απόρριψη σχημάτων ενδοσκόπησης GraphQL, τη δημιουργία προτύπων ερωτημάτων και τον έλεγχο της ασφάλειας GraphQL API</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & GraphQL Security Fundamentals](#-overview--graphql-security-fundamentals)
  - [Κίνδυνοι ερωτήματος ενδοσκόπησης](#introspection-query-hazards)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Introspection Dumping & Schema Generation](#-introspection-dumping--schema-generation)
  - [Ερώτημα ενδοσκόπησης CLI](#cli-introspection-query)
  - [Damping to Schema Definition Language (SDL)](#dumping-to-schema-definition-language-sdl)
  - [Δημιουργία προτύπων ερωτημάτων έτοιμων για εκτέλεση](#generating-ready-to-run-query-templates)
- [Auditing Mutations & Sensitive Queries](#-auditing-mutations--sensitive-queries)
- [Detecting Denial of Service & Batching Flaws](#-detecting-denial-of-service--batching-flaws)
  - [Batch Querying (Array Batching)](#batch-querying-array-batching)
  - [Circular References & Nested Queries](#circular-references--nested-queries)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Hardening](#-defensive-considerations--hardening)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Βασικές αρχές ασφάλειας GraphQL

Σε αντίθεση με τα τυπικά API REST με διακριτές διευθύνσεις URL (`/api/v1/users`), το GraphQL εκθέτει ένα μόνο τελικό σημείο HTTP (συνήθως `/graphql`) όπου οι πελάτες ζητούν προσαρμοσμένα σχήματα δεδομένων μέσω ερωτημάτων και μεταλλάξεων.

### Κίνδυνοι ερωτημάτων ενδοσκόπησης
Το GraphQL διαθέτει ένα ενσωματωμένο σύστημα ενδοσκόπησης (`__schema`, `__type`) σχεδιασμένο για εργαλεία προγραμματιστών. Εάν αφεθεί ενεργοποιημένο στην παραγωγή:
- Οι εισβολείς μπορούν να υποβάλουν ερωτήματα σε ολόκληρο το σχήμα υποστήριξης.
- Οι μη δημοσιευμένες δυνατότητες, οι εσωτερικές μεταλλάξεις διαχείρισης (π.χ. `deleteUser`, `changeRole`) και τα ιδιωτικά πεδία είναι πλήρως εκτεθειμένα.

Δημιουργήθηκε από τον **Doyensec**, ο **InQL** αυτοματοποιεί την εξαγωγή ενδοσκόπησης, αναλύει σχήματα, δημιουργεί τεκμηρίωση και δημιουργεί έτοιμα προς χρήση πρότυπα εκμετάλλευσης.

---

## 📥 Εγκατάσταση
```bash
# Using Python pip (Recommended)
pip install inql

# Or clone from GitHub
git clone https://github.com/doyensec/inql.git
cd inql && pip install .

# Verify installation
inql -h
```
---

## 🚀 Introspection Dumping & Schema Generation

### Ερώτημα ενδοσκόπησης CLI
Υποβάλετε ερώτημα σε ένα απομακρυσμένο τελικό σημείο GraphQL, εξαγάγετε το πλήρες σχήμα και αποθηκεύστε το σε έναν φάκελο εξόδου:
```bash
inql -t https://target.com/graphql -o /tmp/graphql_audit/
```
*Δημιουργήθηκε στον κατάλογο εξόδου:*
- `schema.json`: Απόθεμα ενδοσκόπησης ακατέργαστου JSON.
- `schema.graphql`: Αναγνώσιμη από τον άνθρωπο Γλώσσα ορισμού σχήματος (SDL).
- `queries/`: Πρότυπα ερωτημάτων που δημιουργούνται αυτόματα για κάθε διαθέσιμο ερώτημα.
- `mutations/`: Πρότυπα μετάλλαξης που δημιουργούνται αυτόματα για τροποποίηση δεδομένων.

### Επαληθευμένη σάρωση και προσαρμοσμένες κεφαλίδες
Διαβίβαση cookie περιόδου λειτουργίας, διακριτικά εξουσιοδότησης ή προσαρμοσμένες κεφαλίδες:
```bash
inql -t https://target.com/graphql \
  -H "Authorization: Bearer <TOKEN>" \
  -H "X-API-Key: secret123" \
  -o ./audit_report/
```
### Dumping to Schema Definition Language (SDL)
Μετατροπή υπάρχοντος ακατέργαστου σχήματος JSON σε SDL:
```bash
inql -f schema.json --generate-sdl -o ./sdl_output/
```
---

## 💥 Έλεγχος μεταλλάξεων και ευαίσθητων ερωτημάτων

Ελέγξτε τα αρχεία μετάλλαξης που δημιουργούνται αυτόματα στον φάκελο εξόδου:
```text
mutations/
├── registerUser.graphql
├── resetPassword.graphql
├── updateProfile.graphql
└── adminPromoteUser.graphql
```
Στείλτε μεταλλάξεις υψηλής επίδρασης χρησιμοποιώντας curl ή Burp Suite:
```bash
curl -X POST https://target.com/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "mutation { adminPromoteUser(userId: 10, role: \"SUPERADMIN\") { success } }"}'
```
---

## ⚡ Ανίχνευση άρνησης υπηρεσίας και ατέλειες παρτίδας

### Ερωτήματα παρτίδας (Μαζική παρτίδα)
Ελέγξτε εάν ο διακομιστής επεξεργάζεται πίνακες ερωτημάτων σε ένα μόνο αίτημα HTTP (παρακάμπτει τον περιορισμό του ρυθμού):
```json
[
  {"query": "query { getUser(id: 1) { email } }"},
  {"query": "query { getUser(id: 2) { email } }"},
  {"query": "query { getUser(id: 3) { email } }"}
]
```
### Κυκλικές αναφορές & Ένθετα ερωτήματα
Δοκιμή για σφάλματα αναδρομικών σχέσεων:
```graphql
query {
  thread {
    messages {
      thread {
        messages {
          thread {
            messages {
              id
            }
          }
        }
      }
    }
  }
}
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Απόθεση ενδοσκόπησης** | `inql -t https://target.com/graphql -o ./out/` |
| **Αυθεντικοποιημένη ενδοσκόπηση**| `inql -t <URL> -H "Authorization: Bearer <TOKEN>" -o ./out/` |
| **Μετατροπή σχήματος JSON σε SDL** | `inql -f schema.json --generate-sdl -o ./sdl/` |
| **Δημιουργία εγγράφων HTML** | `inql -t <URL> --generate-html -o ./docs/` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Σκλήρυνση

1. **Απενεργοποιήστε την ενδοσκόπηση στην παραγωγή:** Απενεργοποιήστε το `introspection` στη διαμόρφωση του διακομιστή GraphQL (Apollo, Yoga, GraphQL-Java).
2. **Εφαρμογή ανάλυσης βάθους και κόστους ερωτημάτων:** Εφαρμόστε βιβλιοθήκες όπως το `graphql-depth-limit` ή ανάλυση κόστους ερωτημάτων για να απορρίψετε ερωτήματα με βαθύ ένθετο.
3. **Disable Batching:** Περιορίστε ή απενεργοποιήστε την εκτέλεση ερωτημάτων δέσμης βάσει πίνακα, εάν δεν είναι απολύτως απαραίτητο.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το InQL έχει αναπτυχθεί για εξουσιοδοτημένες αξιολογήσεις ασφάλειας, δοκιμές διείσδυσης και έλεγχο ασφαλείας API. Η εξαγωγή σχημάτων ή η τροποποίηση δεδομένων σε μη εξουσιοδοτημένα τελικά σημεία είναι παράνομη.