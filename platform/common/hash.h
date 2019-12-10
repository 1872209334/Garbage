/*****************************************************************************
Copyright (C), LGF. Co., Ltd.
File name    £º hash.h
Description  :
Author       £ºlgf
Version      £º
Date         £º2017.10.30
Others       £ºEmail:
History      £º
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
1) Date£º
   Mender£º
   Content:
2£©...

*****************************************************************************/
#ifndef HASH_H
#define HASH_H

/* Forward declarations of structures. */
typedef struct Hash Hash;
typedef struct HashElem HashElem;

struct Hash {
    unsigned int htsize;      /* Number of buckets in the hash table */
    unsigned int count;       /* Number of entries in this table */
    HashElem *first;          /* The first element of the array */
    struct _ht {              /* the hash table */
        int count;            /* Number of entries with this hash */
        HashElem *chain;      /* Pointer to first entry with this hash */
    } *ht;
};

/* Each element in the hash table is an instance of the following
** structure.  All elements are stored on a single doubly-linked list.
**
** Again, this structure is intended to be opaque, but it can't really
** be opaque because it is used by macros.
*/
struct HashElem {
    HashElem *next, *prev;       /* Next and previous elements in the table */
    void *data;                  /* Data associated with this element */
    const char *pKey;            /* Key associated with this element */
};

/*
** Access routines.  To delete, insert a NULL pointer.
*/
void HashInit(Hash*);
void *HashInsert(Hash*, const char *pKey, void *pData);
void *HashFind(const Hash*, const char *pKey);
void HashClear(Hash*);

/*
** Macros for looping over all elements of a hash table.  The idiom is
** like this:
**
**   Hash h;
**   HashElem *p;
**   ...
**   for(p=HashFirst(&h); p; p=HashNext(p)){
**     SomeStructure *pData = HashData(p);
**     // do something with pData
**   }
*/
#define HashFirst(H)  ((H)->first)
#define HashNext(E)   ((E)->next)
#define HashData(E)   ((E)->data)
/* #define HashKey(E)    ((E)->pKey) // NOT USED */
/* #define HashKeysize(E) ((E)->nKey)  // NOT USED */

/*
** Number of entries in a hash table
*/
/* #define HashCount(H)  ((H)->count) // NOT USED */

#endif /* HASH_H */
