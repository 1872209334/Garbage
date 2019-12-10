/*****************************************************************************
Copyright (C), LGF. Co., Ltd.
File name    £º hash.c
Description  : This is the implementation of generic hash-tables used in upload.
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
#include <string.h>
#include <stdlib.h>
#include <stddef.h>
#include <assert.h>
#include "hash.h"


#define HASH_MALLOC_SOFT_LIMIT  0

/*
** Check to see if this machine uses EBCDIC.  (Yes, believe it or
** not, there are still machines out there that use EBCDIC.)
*/
#if 'A' == '\301'
# define UPPERTOLOWER_EBCDIC 1
#else
# define UPPERTOLOWER_ASCII 1
#endif

/*****************************************************************************
*****************************************************************************/
static const unsigned char UpperToLower[] = {
#ifdef UPPERTOLOWER_ASCII
      0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17,
     18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
     36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53,
     54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 97, 98, 99,100,101,102,103,
    104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,
    122, 91, 92, 93, 94, 95, 96, 97, 98, 99,100,101,102,103,104,105,106,107,
    108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,
    126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,
    144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,
    162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,
    180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,
    198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,
    216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,
    234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,
    252,253,254,255
#endif
#ifdef UPPERTOLOWER_EBCDIC
      0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, /* 0x */
     16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, /* 1x */
     32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, /* 2x */
     48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, /* 3x */
     64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, /* 4x */
     80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, /* 5x */
     96, 97, 98, 99,100,101,102,103,104,105,106,107,108,109,110,111, /* 6x */
    112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127, /* 7x */
    128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143, /* 8x */
    144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159, /* 9x */
    160,161,162,163,164,165,166,167,168,169,170,171,140,141,142,175, /* Ax */
    176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191, /* Bx */
    192,129,130,131,132,133,134,135,136,137,202,203,204,205,206,207, /* Cx */
    208,145,146,147,148,149,150,151,152,153,218,219,220,221,222,223, /* Dx */
    224,225,162,163,164,165,166,167,168,169,234,235,236,237,238,239, /* Ex */
    240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255, /* Fx */
#endif
};

/*****************************************************************************
Function      : hash_malloc
Description   :
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static void *hash_malloc(size_t n)
{
    if (n == 0) {
        return NULL;
    }

    return malloc(n);
}

/*****************************************************************************
Function      : hash_mfree
Description   :
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static void hash_mfree(void *ptr)
{
    if (ptr) {
        free(ptr);
    }
}

/*****************************************************************************
Function      : hashStrICmp
Description   :
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static int hashStrICmp(const char *zLeft, const char *zRight)
{
    return strcmp(zLeft, zRight);
}

/*****************************************************************************
Function      : HashInit
Description   : Turn bulk memory into a hash table object by initializing the
                fields of the Hash structure.
Input         :
Output        :
Return        :
Others        : "pNew" is a pointer to the hash table that is to be initialized.
*****************************************************************************/
void HashInit(Hash *pNew)
{
    assert( pNew!=0 );
    pNew->first = 0;
    pNew->count = 0;
    pNew->htsize = 0;
    pNew->ht = 0;
}

/*****************************************************************************
Function      : HashClear
Description   : Remove all entries from a hash table.  Reclaim all memory.
                Call this routine to delete a hash table or to reset a hash table
                to the empty state.
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
void HashClear(Hash *pH)
{
    HashElem *elem;

    assert( pH!=0 );
    elem = pH->first;
    pH->first = 0;
    hash_mfree(pH->ht);
    pH->ht = 0;
    pH->htsize = 0;
    while ( elem ) {
        HashElem *next_elem = elem->next;
        hash_mfree(elem);
        elem = next_elem;
    }
    pH->count = 0;
}

/*****************************************************************************
Function      : strHash
Description   : The hashing function.
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static unsigned int strHash(const char *z)
{
    unsigned int h = 0;
    unsigned char c;
    while ( (c = (unsigned char)*z++)!=0 ) {     /*OPTIMIZATION-IF-TRUE*/
      /* Knuth multiplicative hashing.  (Sorting & Searching, p. 510).
       ** 0x9e3779b1 is 2654435761 which is the closest prime number to
       ** (2**32)*golden_ratio, where golden_ratio = (sqrt(5) - 1)/2. */
      h += UpperToLower[c];
      h *= 0x9e3779b1;
    }
    return h;
}

/*****************************************************************************
Function      : insertElement
Description   : Link pNew element into the hash table pH.  If pEntry!=0 then also
                insert pNew into the pEntry hash bucket.
Input         : pH     -- The complete hash table
                pEntry -- The entry into which pNew is inserted
                pNew   -- The element to be inserted
Output        :
Return        :
Others        :
*****************************************************************************/
static void insertElement(Hash *pH, struct _ht *pEntry, HashElem *pNew)
{
    HashElem *pHead;

    if( pEntry ){
        pHead = pEntry->count ? pEntry->chain : 0;
        pEntry->count++;
        pEntry->chain = pNew;
    }else{
        pHead = 0;
    }

    if ( pHead ) {
        pNew->next = pHead;
        pNew->prev = pHead->prev;
        if( pHead->prev ){ pHead->prev->next = pNew; }
        else             { pH->first = pNew; }
        pHead->prev = pNew;
    }else{
        pNew->next = pH->first;
        if( pH->first ){ pH->first->prev = pNew; }
        pNew->prev = 0;
        pH->first = pNew;
    }
}

/*****************************************************************************
Function      : rehash
Description   : Resize the hash table so that it cantains "new_size" buckets.
                The hash table might fail to resize if hash_malloc() fails or
                if the new size is the same as the prior size.
Input         :
Output        :
Return        : TRUE if the resize occurs and false if not.
Others        :
*****************************************************************************/
static int rehash(Hash *pH, unsigned int new_size)
{
    struct _ht *new_ht;
    HashElem *elem, *next_elem;

#if HASH_MALLOC_SOFT_LIMIT > 0
    if ( new_size*sizeof(struct _ht) > HASH_MALLOC_SOFT_LIMIT ) {
        new_size = HASH_MALLOC_SOFT_LIMIT/sizeof(struct _ht);
    }
    if ( new_size == pH->htsize ) {
        return 0;
    }
#endif

    new_ht = (struct _ht *)hash_malloc( new_size*sizeof(struct _ht) );
    if ( new_ht == NULL ) {
        return 0;
    }
    hash_mfree(pH->ht);
    pH->ht = new_ht;
    pH->htsize = new_size;
    memset(new_ht, 0, new_size*sizeof(struct _ht));
    for (elem = pH->first, pH->first = 0; elem; elem = next_elem) {
        unsigned int h = strHash(elem->pKey) % new_size;
        next_elem = elem->next;
        insertElement(pH, &new_ht[h], elem);
    }
    return 1;
}

/*****************************************************************************
Function      : findElementWithHash
Description   : This function (for internal use only) locates an element in an
                hash table that matches the given key.  If no element is found,
                a pointer to a static null element with HashElem.data==0 is returned.
                If pH is not NULL, then the hash for this key is written to *pH.
Input         : pH    -- The pH to be searched
                pKey  -- The key we are searching for
                pHash -- Write the hash value here
Output        :
Return        :
Others        :
*****************************************************************************/
static HashElem *findElementWithHash(const Hash *pH, const char *pKey,unsigned int *pHash)
{
    HashElem *elem;
    int count;
    unsigned int h;

    static HashElem nullElement = { 0, 0, 0, 0 };

    if ( pH->ht ) {   /*OPTIMIZATION-IF-TRUE*/
        struct _ht *pEntry;
        h = strHash(pKey) % pH->htsize;
        pEntry = &pH->ht[h];
        elem = pEntry->chain;
        count = pEntry->count;
    }else{
        h = 0;
        elem = pH->first;
        count = pH->count;
    }

    if( pHash ) {
        *pHash = h;
    }

    while( count-- ){
        assert(elem != 0);
        if (hashStrICmp(elem->pKey, pKey) == 0) {
            return elem;
        }
        elem = elem->next;
    }

    return &nullElement;
}

/*****************************************************************************
Function      : removeElementGivenHash
Description   : Remove a single entry from the hash table given a pointer to that
                element and a hash on the element's key.
Input         : pH    -- The pH containing "elem"
                elem  -- The element to be removed from the pH
                h     -- Hash value for the element
Output        :
Return        :
Others        :
*****************************************************************************/
static void removeElementGivenHash(Hash *pH, HashElem* elem, unsigned int h)
{
    struct _ht *pEntry;

    if ( elem->prev ) {
        elem->prev->next = elem->next;
    } else {
        pH->first = elem->next;
    }
    if ( elem->next ) {
        elem->next->prev = elem->prev;
    }

    if ( pH->ht ) {
        pEntry = &pH->ht[h];
        if ( pEntry->chain == elem ) {
            pEntry->chain = elem->next;
        }
        pEntry->count--;
        assert( pEntry->count>=0 );
    }
    hash_mfree(elem);
    pH->count--;
    if ( pH->count == 0 ) {
        assert( pH->first==0 );
        assert( pH->count==0 );
        HashClear(pH);
    }
}

/*****************************************************************************
Function      : HashFind
Description   : Attempt to locate an element of the hash table pH with a key
                that matches pKey.  Return the data for this element if it is
                found, or NULL if there is no match.
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
void *HashFind(const Hash *pH, const char *pKey)
{
    assert( pH!=0 );
    assert( pKey!=0 );
    return findElementWithHash(pH, pKey, 0)->data;
}

/*****************************************************************************
Function      : HashInsert
Description   : Insert an element into the hash table pH.  The key is pKey
                and the data is "data".
                If no element exists with a matching key, then a new
                element is created and NULL is returned.

                If another element already exists with the same key, then the
                new data replaces the old data and the old data is returned.
                The key is not copied in this instance.  If a malloc fails, then
                the new data is returned and the hash table is unchanged.

                If the "data" parameter to this function is NULL, then the
                element corresponding to "key" is removed from the hash table.
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
void *HashInsert(Hash *pH, const char *pKey, void *data)
{
    unsigned int h;
    HashElem *elem;
    HashElem *new_elem;

    assert( pH!=0 );
    assert( pKey!=0 );
    elem = findElementWithHash(pH,pKey,&h);
    if ( elem->data ) {
        void *old_data = elem->data;
        if( data==0 ){
            removeElementGivenHash(pH,elem,h);
        } else{
            elem->data = data;
            elem->pKey = pKey;
        }
        return old_data;
    }

    if ( data==0 ) {
        return 0;
    }

    new_elem = (HashElem*)hash_malloc( sizeof(HashElem) );
    if ( new_elem == 0 ) {
        return data;
    }
    new_elem->pKey = pKey;
    new_elem->data = data;
    pH->count++;
    if ( pH->count >= 10 && pH->count > 2*pH->htsize ) {
        if ( rehash(pH, pH->count*2) ) {
            assert( pH->htsize > 0 );
            h = strHash(pKey) % pH->htsize;
        }
    }
    insertElement(pH, pH->ht ? &pH->ht[h] : 0, new_elem);
    return 0;
}
