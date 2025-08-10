"use client"

import { useEffect, useMemo, useState } from "react"
import { UsersAPI } from "@/lib/api-client"
import type { User } from "@/lib/types"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { useToast } from "@/hooks/use-toast"
import { Badge } from "@/components/ui/badge"
import { Trash2, RefreshCw, UserPlus, Search, Save } from "lucide-react"

export default function UsersPanel() {
  const { toast } = useToast()
  const [users, setUsers] = useState<User[]>([])
  const [loading, setLoading] = useState(false)

  const [createBody, setCreateBody] = useState<Pick<User, "username" | "name" | "email">>({
    username: "",
    name: "",
    email: "",
  })

  const [findById, setFindById] = useState("")
  const [foundById, setFoundById] = useState<User | null>(null)

  const [findByUsername, setFindByUsername] = useState("")
  const [foundByUsername, setFoundByUsername] = useState<User | null>(null)

  const [updateBody, setUpdateBody] = useState<Pick<User, "username" | "name" | "email">>({
    username: "",
    name: "",
    email: "",
  })

  const [updateUsernameBody, setUpdateUsernameBody] = useState<{ oldUsername: string; newUsername: string }>({
    oldUsername: "",
    newUsername: "",
  })

  async function loadUsers() {
    setLoading(true)
    try {
      const res = await UsersAPI.getAllUsers()
      setUsers(res.data ?? [])
    } catch (e: any) {
      toast({ title: "Failed to load users", description: String(e.message), variant: "destructive" })
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadUsers()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  async function handleCreate() {
    try {
      console.log("Button clicked — starting API call");
      const res = await UsersAPI.createUser(createBody)
      console.log("API response", await res);
      toast({ title: "Success", description: res.message })
      setCreateBody({ username: "", name: "", email: "" })
      await loadUsers()
    } catch (e: any) {
      toast({ title: "Create failed", description: String(e.message), variant: "destructive" })
    }
  }

  async function handleFindById() {
    setFoundById(null)
    if (!findById) return
    try {
      const res = await UsersAPI.findUserById(findById)
      setFoundById(res.data)
    } catch (e: any) {
      toast({ title: "Find by ID failed", description: String(e.message), variant: "destructive" })
    }
  }

  async function handleFindByUsername() {
    setFoundByUsername(null)
    if (!findByUsername) return
    try {
      const res = await UsersAPI.findUserByUsername(findByUsername)
      setFoundByUsername(res.data)
    } catch (e: any) {
      toast({ title: "Find by username failed", description: String(e.message), variant: "destructive" })
    }
  }

  async function handleUpdateUser() {
    try {
      const res = await UsersAPI.updateUser(updateBody)
      toast({ title: "Updated", description: res.message })
      await loadUsers()
    } catch (e: any) {
      toast({ title: "Update failed", description: String(e.message), variant: "destructive" })
    }
  }

  async function handleUpdateUsername() {
    try {
      const res = await UsersAPI.updateUsername(updateUsernameBody)
      toast({ title: "Username updated", description: res.message })
      await loadUsers()
    } catch (e: any) {
      toast({ title: "Update username failed", description: String(e.message), variant: "destructive" })
    }
  }

  async function handleDelete(username: string) {
    if (!username) return
    try {
      const res = await UsersAPI.deleteUser(username)
      toast({ title: "Deleted", description: res.message })
      await loadUsers()
    } catch (e: any) {
      toast({ title: "Delete failed", description: String(e.message), variant: "destructive" })
    }
  }

  const sortedUsers = useMemo(() => {
    return [...users].sort((a, b) => a.username.localeCompare(b.username))
  }, [users])

  return (
    <div className="grid gap-6">
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <UserPlus className="h-5 w-5" /> Create User
          </CardTitle>
          <CardDescription>Provide username, name, and email.</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-3 sm:grid-cols-3">
          <div className="grid gap-2">
            <Label htmlFor="c-username">Username</Label>
            <Input
              id="c-username"
              value={createBody.username}
              onChange={(e) => setCreateBody({ ...createBody, username: e.target.value })}
            />
          </div>
          <div className="grid gap-2">
            <Label htmlFor="c-name">Name</Label>
            <Input
              id="c-name"
              value={createBody.name}
              onChange={(e) => setCreateBody({ ...createBody, name: e.target.value })}
            />
          </div>
          <div className="grid gap-2">
            <Label htmlFor="c-email">Email</Label>
            <Input
              id="c-email"
              type="email"
              value={createBody.email}
              onChange={(e) => setCreateBody({ ...createBody, email: e.target.value })}
            />
          </div>
          <div className="sm:col-span-3 flex justify-end">
            <Button onClick={handleCreate}>Create</Button>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader className="flex flex-col gap-1 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <CardTitle className="flex items-center gap-2">
              <Search className="h-5 w-5" /> Find User
            </CardTitle>
            <CardDescription>Search by ID or username.</CardDescription>
          </div>
        </CardHeader>
        <CardContent className="grid gap-4 sm:grid-cols-2">
          <div className="grid gap-2">
            <Label htmlFor="find-id">User ID</Label>
            <div className="flex gap-2">
              <Input
                id="find-id"
                placeholder="uuid..."
                value={findById}
                onChange={(e) => setFindById(e.target.value)}
              />
              <Button variant="secondary" onClick={handleFindById}>
                Find
              </Button>
            </div>
            {foundById && (
              <div className="text-sm">
                <p>
                  <span className="font-medium">Username:</span> {foundById.username}
                </p>
                <p>
                  <span className="font-medium">Name:</span> {foundById.name}
                </p>
                <p>
                  <span className="font-medium">Email:</span> {foundById.email}
                </p>
              </div>
            )}
          </div>
          <div className="grid gap-2">
            <Label htmlFor="find-username">Username</Label>
            <div className="flex gap-2">
              <Input id="find-username" value={findByUsername} onChange={(e) => setFindByUsername(e.target.value)} />
              <Button variant="secondary" onClick={handleFindByUsername}>
                Find
              </Button>
            </div>
            {foundByUsername && (
              <div className="text-sm">
                <p>
                  <span className="font-medium">ID:</span> {foundByUsername.id}
                </p>
                <p>
                  <span className="font-medium">Name:</span> {foundByUsername.name}
                </p>
                <p>
                  <span className="font-medium">Email:</span> {foundByUsername.email}
                </p>
              </div>
            )}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Save className="h-5 w-5" /> Update User
          </CardTitle>
          <CardDescription>Update name/email by username, or rename a username.</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-6">
          <div className="grid gap-3 sm:grid-cols-3">
            <div className="grid gap-2">
              <Label htmlFor="u-username">Username</Label>
              <Input
                id="u-username"
                value={updateBody.username}
                onChange={(e) => setUpdateBody({ ...updateBody, username: e.target.value })}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="u-name">Name</Label>
              <Input
                id="u-name"
                value={updateBody.name}
                onChange={(e) => setUpdateBody({ ...updateBody, name: e.target.value })}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="u-email">Email</Label>
              <Input
                id="u-email"
                type="email"
                value={updateBody.email}
                onChange={(e) => setUpdateBody({ ...updateBody, email: e.target.value })}
              />
            </div>
            <div className="sm:col-span-3 flex justify-end">
              <Button onClick={handleUpdateUser}>Update</Button>
            </div>
          </div>
          <div className="grid gap-3 sm:grid-cols-3">
            <div className="grid gap-2">
              <Label htmlFor="uu-old">Old Username</Label>
              <Input
                id="uu-old"
                value={updateUsernameBody.oldUsername}
                onChange={(e) => setUpdateUsernameBody({ ...updateUsernameBody, oldUsername: e.target.value })}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="uu-new">New Username</Label>
              <Input
                id="uu-new"
                value={updateUsernameBody.newUsername}
                onChange={(e) => setUpdateUsernameBody({ ...updateUsernameBody, newUsername: e.target.value })}
              />
            </div>
            <div className="flex items-end">
              <Button onClick={handleUpdateUsername}>Rename</Button>
            </div>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader className="flex flex-col gap-1 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <CardTitle>All Users</CardTitle>
            <CardDescription>List of users from the server.</CardDescription>
          </div>
          <div className="flex items-center gap-2">
            <Badge variant="outline">{users.length} users</Badge>
            <Button variant="outline" size="icon" onClick={loadUsers} disabled={loading} aria-label="Refresh">
              <RefreshCw className={`h-4 w-4 ${loading ? "animate-spin" : ""}`} />
            </Button>
          </div>
        </CardHeader>
        <CardContent className="relative overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Username</TableHead>
                <TableHead>Name</TableHead>
                <TableHead>Email</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {sortedUsers.map((u) => (
                <TableRow key={u.id ?? u.username}>
                  <TableCell className="font-medium">{u.username}</TableCell>
                  <TableCell>{u.name}</TableCell>
                  <TableCell>{u.email}</TableCell>
                  <TableCell className="text-right">
                    <Button
                      variant="destructive"
                      size="icon"
                      onClick={() => handleDelete(u.username)}
                      aria-label={`Delete ${u.username}`}
                    >
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
              {sortedUsers.length === 0 && (
                <TableRow>
                  <TableCell colSpan={4} className="text-center text-sm text-muted-foreground">
                    No users found.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  )
}
