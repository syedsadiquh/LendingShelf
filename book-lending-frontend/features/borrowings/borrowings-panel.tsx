"use client"

import { useEffect, useState } from "react"
import { BorrowingsAPI } from "@/lib/api-client"
import type { Borrowing } from "@/lib/types"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { useToast } from "@/hooks/use-toast"
import { Handshake, LogIn, RefreshCw, RotateCcw, UserSearch } from "lucide-react"

export default function BorrowingsPanel() {
  const { toast } = useToast()

  const [all, setAll] = useState<Borrowing[]>([])
  const [loading, setLoading] = useState(false)

  // Create
  const [createBody, setCreateBody] = useState<{ book_id: string; user_id: string }>({ book_id: "", user_id: "" })

  // Return
  const [returnId, setReturnId] = useState("")

  // Active
  const [activeUserId, setActiveUserId] = useState("")
  const [active, setActive] = useState<Borrowing[]>([])

  async function loadAll() {
    setLoading(true)
    try {
      const res = await BorrowingsAPI.getAllBorrowing()
      setAll(res.data ?? [])
    } catch (e: any) {
      toast({ title: "Load failed", description: String(e.message), variant: "destructive" })
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadAll()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  async function handleCreate() {
    try {
      const res = await BorrowingsAPI.createBorrowing(createBody)
      toast({ title: "Created", description: res.message })
      setCreateBody({ book_id: "", user_id: "" })
      await loadAll()
    } catch (e: any) {
      toast({ title: "Create failed", description: String(e.message), variant: "destructive" })
    }
  }

  async function handleReturn() {
    if (!returnId) return
    try {
      const res = await BorrowingsAPI.returnBorrowing(returnId)
      toast({ title: "Returned", description: res.message })
      setReturnId("")
      await loadAll()
    } catch (e: any) {
      toast({ title: "Return failed", description: String(e.message), variant: "destructive" })
    }
  }

  async function handleGetActive() {
    setActive([])
    if (!activeUserId) return
    try {
      const res = await BorrowingsAPI.getActiveBorrowing(activeUserId)
      setActive(res.data ?? [])
      toast({ title: "Fetched", description: res.message })
    } catch (e: any) {
      toast({ title: "Fetch failed", description: String(e.message), variant: "destructive" })
    }
  }

  return (
    <div className="grid gap-6">
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Handshake className="h-5 w-5" /> Create Borrowing
          </CardTitle>
          <CardDescription>Provide book_id and user_id.</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-3 sm:grid-cols-3">
          <div className="grid gap-2">
            <Label htmlFor="cb-book">Book ID</Label>
            <Input
              id="cb-book"
              placeholder="uuid..."
              value={createBody.book_id}
              onChange={(e) => setCreateBody({ ...createBody, book_id: e.target.value })}
            />
          </div>
          <div className="grid gap-2">
            <Label htmlFor="cb-user">User ID</Label>
            <Input
              id="cb-user"
              placeholder="uuid..."
              value={createBody.user_id}
              onChange={(e) => setCreateBody({ ...createBody, user_id: e.target.value })}
            />
          </div>
          <div className="flex items-end">
            <Button onClick={handleCreate}>
              <LogIn className="mr-2 h-4 w-4" />
              Create
            </Button>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader className="flex flex-col gap-1 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <CardTitle>All Borrowings</CardTitle>
            <CardDescription>List of borrowings.</CardDescription>
          </div>
          <Button variant="outline" size="icon" onClick={loadAll} disabled={loading} aria-label="Refresh">
            <RefreshCw className={`h-4 w-4 ${loading ? "animate-spin" : ""}`} />
          </Button>
        </CardHeader>
        <CardContent className="relative overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Borrowing ID</TableHead>
                <TableHead>User</TableHead>
                <TableHead>Book</TableHead>
                <TableHead>Expected Return</TableHead>
                <TableHead>Actual Return</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {all.map((b) => (
                <TableRow key={b.id}>
                  <TableCell className="font-medium">{b.id}</TableCell>
                  <TableCell>
                    {b.user?.username} ({b.user?.email})
                  </TableCell>
                  <TableCell>
                    {b.book?.title} by {b.book?.author}
                  </TableCell>
                  <TableCell>{new Date(b.expectedReturnDate).toLocaleString()}</TableCell>
                  <TableCell>{b.actualReturnDate ? new Date(b.actualReturnDate).toLocaleString() : "—"}</TableCell>
                </TableRow>
              ))}
              {!all.length && (
                <TableRow>
                  <TableCell colSpan={5} className="text-center text-sm text-muted-foreground">
                    No borrowings found.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <RotateCcw className="h-5 w-5" /> Return Borrowing
          </CardTitle>
          <CardDescription>Provide borrowing_id to return.</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-3 sm:grid-cols-3">
          <div className="grid gap-2 sm:col-span-2">
            <Label htmlFor="rb-id">Borrowing ID</Label>
            <Input id="rb-id" placeholder="uuid..." value={returnId} onChange={(e) => setReturnId(e.target.value)} />
          </div>
          <div className="flex items-end">
            <Button onClick={handleReturn}>Return</Button>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <UserSearch className="h-5 w-5" /> Active Borrowings by User
          </CardTitle>
          <CardDescription>Provide user_id to fetch active borrowings.</CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid gap-3 sm:grid-cols-3">
            <div className="grid gap-2 sm:col-span-2">
              <Label htmlFor="ab-user">User ID</Label>
              <Input
                id="ab-user"
                placeholder="uuid..."
                value={activeUserId}
                onChange={(e) => setActiveUserId(e.target.value)}
              />
            </div>
            <div className="flex items-end">
              <Button onClick={handleGetActive}>Fetch</Button>
            </div>
          </div>
          <div className="relative overflow-x-auto">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Borrowing ID</TableHead>
                  <TableHead>Book</TableHead>
                  <TableHead>Expected Return</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {active.map((b) => (
                  <TableRow key={b.id}>
                    <TableCell className="font-medium">{b.id}</TableCell>
                    <TableCell>
                      {b.book?.title} by {b.book?.author}
                    </TableCell>
                    <TableCell>{new Date(b.expectedReturnDate).toLocaleString()}</TableCell>
                  </TableRow>
                ))}
                {!active.length && (
                  <TableRow>
                    <TableCell colSpan={3} className="text-center text-sm text-muted-foreground">
                      No active borrowings.
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
