"use client"

import { useEffect, useMemo, useState } from "react"
import { BooksAPI } from "@/lib/api-client"
import type { Book, Paged } from "@/lib/types"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Badge } from "@/components/ui/badge"
import { useToast } from "@/hooks/use-toast"
import { BookOpen, PlusCircle, RefreshCw, Search, Save, Trash2, Filter } from "lucide-react"

type BooksPageState = {
  sortBy?: string
  page?: number
  size?: number
  ascending?: boolean
}

export default function BooksPanel() {
  const { toast } = useToast()

  // Create
  const [createBody, setCreateBody] = useState({ title: "", author: "", isbn: "", pubYear: 0, quantity: 0 })

  // List
  const [loading, setLoading] = useState(false)
  const [booksPage, setBooksPage] = useState<Paged<Book> | null>(null)
  const [params, setParams] = useState<BooksPageState>({ page: 0, size: 10, sortBy: "author", ascending: true })

  // Update
  const [updateId, setUpdateId] = useState("")
  const [updateBody, setUpdateBody] = useState<
    Partial<{ title: string; author: string; isbn: string; pubYear: number; quantity: number }>
  >({})

  // Find by ISBN
  const [isbn, setIsbn] = useState("")
  const [found, setFound] = useState<Book | null>(null)

  // Search
  const [searchBody, setSearchBody] = useState<
    Partial<{ author: string; title: string; isbn: string; pubYear: number }>
  >({})
  const [searchResults, setSearchResults] = useState<Paged<Book> | null>(null)
  const [searchLoading, setSearchLoading] = useState(false)

  async function loadBooks() {
    setLoading(true)
    try {
      const res = await BooksAPI.getAllBooks(params)
      setBooksPage(res.data)
    } catch (e: any) {
      toast({ title: "Failed to load books", description: String(e.message), variant: "destructive" })
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadBooks()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [params.page, params.size, params.sortBy, params.ascending])

  async function handleCreate() {
    try {
      const res = await BooksAPI.createBook(createBody)
      toast({ title: "Created", description: res.message })
      setCreateBody({ title: "", author: "", isbn: "", pubYear: 0, quantity: 0 })
      await loadBooks()
    } catch (e: any) {
      toast({ title: "Create failed", description: String(e.message), variant: "destructive" })
    }
  }

  async function handleFindByIsbn() {
    setFound(null)
    if (!isbn) return
    try {
      const res = await BooksAPI.findBookByIsbn(isbn)
      setFound(res.data)
    } catch (e: any) {
      toast({ title: "Find failed", description: String(e.message), variant: "destructive" })
    }
  }

  async function handleUpdate() {
    if (!updateId) {
      toast({ title: "Missing ID", description: "Provide book ID to update.", variant: "destructive" })
      return
    }
    try {
      const res = await BooksAPI.updateBook(updateId, updateBody)
      toast({ title: "Updated", description: res.message })
      setUpdateId("")
      setUpdateBody({})
      await loadBooks()
    } catch (e: any) {
      toast({ title: "Update failed", description: String(e.message), variant: "destructive" })
    }
  }

  async function handleDelete(id: string) {
    try {
      const res = await BooksAPI.deleteBook(id)
      toast({ title: "Deleted", description: res.message })
      await loadBooks()
    } catch (e: any) {
      toast({ title: "Delete failed", description: String(e.message), variant: "destructive" })
    }
  }

  async function handleSearch() {
    setSearchResults(null)
    setSearchLoading(true)
    try {
      const res = await BooksAPI.searchBooks(searchBody, { sortBy: "author", page: 0, size: 10, ascending: true })
      setSearchResults(res.data)
      if (!res.data?.content?.length) {
        toast({ title: "No matches", description: "No books found for given criteria." })
      }
    } catch (e: any) {
      toast({
        title: "Search failed",
        description: `Search endpoint expects GET with JSON body. If your environment blocks that, ask backend to allow POST or query filters. Error: ${String(e.message)}`,
        variant: "destructive",
      })
    } finally {
      setSearchLoading(false)
    }
  }

  const books = useMemo(() => booksPage?.content ?? [], [booksPage])

  return (
    <div className="grid gap-6">
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <PlusCircle className="h-5 w-5" /> Create Book
          </CardTitle>
          <CardDescription>Title, author, ISBN, publication year, and quantity.</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-3 sm:grid-cols-5">
          <div className="grid gap-2">
            <Label htmlFor="b-title">Title</Label>
            <Input
              id="b-title"
              value={createBody.title}
              onChange={(e) => setCreateBody({ ...createBody, title: e.target.value })}
            />
          </div>
          <div className="grid gap-2">
            <Label htmlFor="b-author">Author</Label>
            <Input
              id="b-author"
              value={createBody.author}
              onChange={(e) => setCreateBody({ ...createBody, author: e.target.value })}
            />
          </div>
          <div className="grid gap-2">
            <Label htmlFor="b-isbn">ISBN</Label>
            <Input
              id="b-isbn"
              value={createBody.isbn}
              onChange={(e) => setCreateBody({ ...createBody, isbn: e.target.value })}
            />
          </div>
          <div className="grid gap-2">
            <Label htmlFor="b-year">Publication Year</Label>
            <Input
              id="b-year"
              type="number"
              value={createBody.pubYear || ""}
              onChange={(e) => setCreateBody({ ...createBody, pubYear: Number(e.target.value) })}
            />
          </div>
          <div className="grid gap-2">
            <Label htmlFor="b-qty">Quantity</Label>
            <Input
              id="b-qty"
              type="number"
              value={createBody.quantity || ""}
              onChange={(e) => setCreateBody({ ...createBody, quantity: Number(e.target.value) })}
            />
          </div>
          <div className="sm:col-span-5 flex justify-end">
            <Button onClick={handleCreate}>Create</Button>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader className="flex flex-col gap-1 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <CardTitle className="flex items-center gap-2">
              <BookOpen className="h-5 w-5" /> All Books
            </CardTitle>
            <CardDescription>Paginated and sortable list.</CardDescription>
          </div>
          <div className="flex items-center gap-2">
            <Badge variant="outline">{booksPage?.page?.totalElements ?? 0} total</Badge>
            <Button variant="outline" size="icon" onClick={loadBooks} disabled={loading} aria-label="Refresh">
              <RefreshCw className={`h-4 w-4 ${loading ? "animate-spin" : ""}`} />
            </Button>
          </div>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid gap-3 sm:grid-cols-4">
            <div className="grid gap-2">
              <Label>Sort By</Label>
              <Select value={params.sortBy ?? "author"} onValueChange={(v) => setParams((p) => ({ ...p, sortBy: v }))}>
                <SelectTrigger>
                  <SelectValue placeholder="Select field" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="author">author</SelectItem>
                  <SelectItem value="title">title</SelectItem>
                  <SelectItem value="publicationYear">publicationYear</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="grid gap-2">
              <Label>Order</Label>
              <Select
                value={String(params.ascending ?? true)}
                onValueChange={(v) => setParams((p) => ({ ...p, ascending: v === "true" }))}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Select order" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="true">Ascending</SelectItem>
                  <SelectItem value="false">Descending</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="grid gap-2">
              <Label>Page</Label>
              <Input
                type="number"
                min={0}
                value={params.page ?? 0}
                onChange={(e) => setParams((p) => ({ ...p, page: Number(e.target.value) }))}
              />
            </div>
            <div className="grid gap-2">
              <Label>Size</Label>
              <Input
                type="number"
                min={1}
                value={params.size ?? 10}
                onChange={(e) => setParams((p) => ({ ...p, size: Number(e.target.value) }))}
              />
            </div>
          </div>
          <div className="relative overflow-x-auto">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Title</TableHead>
                  <TableHead>Author</TableHead>
                  <TableHead>ISBN</TableHead>
                  <TableHead>Year</TableHead>
                  <TableHead>Available</TableHead>
                  <TableHead className="text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {books.map((b) => (
                  <TableRow key={b.id ?? b.isbn}>
                    <TableCell className="font-medium">{b.title}</TableCell>
                    <TableCell>{b.author}</TableCell>
                    <TableCell>{b.isbn}</TableCell>
                    <TableCell>{b.publicationYear}</TableCell>
                    <TableCell>{b.availableQuantity}</TableCell>
                    <TableCell className="text-right">
                      <Button
                        variant="destructive"
                        size="icon"
                        onClick={() => b.id && handleDelete(b.id)}
                        aria-label="Delete book"
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
                {books.length === 0 && (
                  <TableRow>
                    <TableCell colSpan={6} className="text-center text-sm text-muted-foreground">
                      No books found.
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </div>
          <div className="text-sm text-muted-foreground">
            Page {(booksPage?.page?.number ?? 0) + 1} of {booksPage?.page?.totalPages ?? 1}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Search className="h-5 w-5" /> Find & Update
          </CardTitle>
          <CardDescription>Find by ISBN. Update by ID with partial fields.</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-6">
          <div className="grid gap-3 sm:grid-cols-3">
            <div className="grid gap-2 sm:col-span-2">
              <Label htmlFor="isbn">ISBN</Label>
              <Input id="isbn" value={isbn} onChange={(e) => setIsbn(e.target.value)} />
            </div>
            <div className="flex items-end">
              <Button variant="secondary" onClick={handleFindByIsbn}>
                Find Book
              </Button>
            </div>
            {found && (
              <div className="sm:col-span-3 text-sm">
                <p>
                  <span className="font-medium">ID:</span> {found.id}
                </p>
                <p>
                  <span className="font-medium">Title:</span> {found.title}
                </p>
                <p>
                  <span className="font-medium">Author:</span> {found.author}
                </p>
              </div>
            )}
          </div>
          <div className="grid gap-3 sm:grid-cols-6">
            <div className="grid gap-2 sm:col-span-2">
              <Label htmlFor="u-id">Book ID</Label>
              <Input id="u-id" placeholder="uuid..." value={updateId} onChange={(e) => setUpdateId(e.target.value)} />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="u-title">Title</Label>
              <Input
                id="u-title"
                value={updateBody.title ?? ""}
                onChange={(e) => setUpdateBody({ ...updateBody, title: e.target.value })}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="u-author">Author</Label>
              <Input
                id="u-author"
                value={updateBody.author ?? ""}
                onChange={(e) => setUpdateBody({ ...updateBody, author: e.target.value })}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="u-isbn">ISBN</Label>
              <Input
                id="u-isbn"
                value={updateBody.isbn ?? ""}
                onChange={(e) => setUpdateBody({ ...updateBody, isbn: e.target.value })}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="u-year">Year</Label>
              <Input
                id="u-year"
                type="number"
                value={updateBody.pubYear ?? ""}
                onChange={(e) => setUpdateBody({ ...updateBody, pubYear: Number(e.target.value) })}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="u-qty">Quantity</Label>
              <Input
                id="u-qty"
                type="number"
                value={updateBody.quantity ?? ""}
                onChange={(e) => setUpdateBody({ ...updateBody, quantity: Number(e.target.value) })}
              />
            </div>
            <div className="sm:col-span-6 flex justify-end">
              <Button onClick={handleUpdate}>
                <Save className="mr-2 h-4 w-4" />
                Update
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Filter className="h-5 w-5" /> Search Books
          </CardTitle>
          <CardDescription>Filters sent as JSON body on a GET request per backend spec.</CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid gap-3 sm:grid-cols-5">
            <div className="grid gap-2">
              <Label htmlFor="s-author">Author contains</Label>
              <Input
                id="s-author"
                value={searchBody.author ?? ""}
                onChange={(e) => setSearchBody({ ...searchBody, author: e.target.value })}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="s-title">Title contains</Label>
              <Input
                id="s-title"
                value={searchBody.title ?? ""}
                onChange={(e) => setSearchBody({ ...searchBody, title: e.target.value })}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="s-isbn">ISBN</Label>
              <Input
                id="s-isbn"
                value={searchBody.isbn ?? ""}
                onChange={(e) => setSearchBody({ ...searchBody, isbn: e.target.value })}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="s-year">Publication Year</Label>
              <Input
                id="s-year"
                type="number"
                value={searchBody.pubYear ?? ""}
                onChange={(e) => setSearchBody({ ...searchBody, pubYear: Number(e.target.value) })}
              />
            </div>
            <div className="flex items-end">
              <Button onClick={handleSearch} disabled={searchLoading}>
                {searchLoading ? "Searching..." : "Search"}
              </Button>
            </div>
          </div>
          <div className="relative overflow-x-auto">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Title</TableHead>
                  <TableHead>Author</TableHead>
                  <TableHead>ISBN</TableHead>
                  <TableHead>Year</TableHead>
                  <TableHead>Available</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {(searchResults?.content ?? []).map((b) => (
                  <TableRow key={b.id ?? b.isbn}>
                    <TableCell className="font-medium">{b.title}</TableCell>
                    <TableCell>{b.author}</TableCell>
                    <TableCell>{b.isbn}</TableCell>
                    <TableCell>{b.publicationYear}</TableCell>
                    <TableCell>{b.availableQuantity}</TableCell>
                  </TableRow>
                ))}
                {!searchResults?.content?.length && (
                  <TableRow>
                    <TableCell colSpan={5} className="text-center text-sm text-muted-foreground">
                      No results.
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
