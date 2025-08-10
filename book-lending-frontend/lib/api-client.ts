import type { ApiResponse, User, Book, Borrowing, Paged, CreateBookBody, UpdateBookBody, SearchBookBody } from "./types"

function getBaseUrl() {
  // Priority: localStorage setting > NEXT_PUBLIC_API_BASE > default
  if (typeof window !== "undefined") {
    console.log(window.localStorage)
    const saved = window.localStorage.getItem("apiBaseUrl")
    if (saved && saved.trim()) return saved.trim()
    }
  console.log(process.env.NEXT_PUBLIC_API_BASE!);
  if (typeof process !== "undefined" && process.env && process.env.NEXT_PUBLIC_API_BASE!) {
    return process.env.NEXT_PUBLIC_API_BASE as string
  }
  return "http://localhost:8080"
}

type FetchOptions = {
  method?: "GET" | "POST" | "DELETE"
  headers?: HeadersInit
  body?: any
  signal?: AbortSignal
}

async function request<T>(path: string, opts: FetchOptions = {}): Promise<ApiResponse<T>> {
  const url = `${getBaseUrl()}${path}`
  const init: RequestInit = {
    method: opts.method ?? "GET",
    headers: {
      "Content-Type": "application/json",
      ...(opts.headers ?? {}),
    },
    signal: opts.signal,
  }
  if (opts.body !== undefined) {
    init.body = typeof opts.body === "string" ? opts.body : JSON.stringify(opts.body)
  }

  const res = await fetch(url, init)
  const contentType = res.headers.get("content-type") ?? ""
  const isJson = contentType.includes("application/json")
  const payload = isJson ? await res.json() : null

  if (!res.ok) {
    const message = payload?.message ?? `Request failed with status ${res.status}`
    // Maintain consistent structure
    throw new Error(message)
  }

  return payload as ApiResponse<T>
}

// Users
export const UsersAPI = {
  createUser: (body: Pick<User, "username" | "name" | "email">) =>
    request<Pick<User, "username" | "name" | "email">>("/v1/user/createUser", { method: "POST", body }),

  getAllUsers: () => request<User[]>("/v1/user/getAllUsers"),

  findUserById: (id: string) => request<User>(`/v1/user/findUserById?id=${encodeURIComponent(id)}`),

  findUserByUsername: (username: string) =>
    request<User>(`/v1/user/findUserByUsername?username=${encodeURIComponent(username)}`),

  updateUser: (body: Pick<User, "username" | "name" | "email">) =>
    request<User>("/v1/user/updateUser", { method: "POST", body }),

  updateUsername: (body: { oldUsername: string; newUsername: string }) =>
    request<User>("/v1/user/updateUsername", { method: "POST", body }),

  deleteUser: (username: string) =>
    request<null>(`/v1/user/deleteUser?username=${encodeURIComponent(username)}`, { method: "DELETE" }),
}

// Books
function buildQuery(params: Record<string, any>) {
  const usp = new URLSearchParams()
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== null && v !== "") {
      usp.set(k, String(v))
    }
  })
  const qs = usp.toString()
  return qs ? `?${qs}` : ""
}

export const BooksAPI = {
  createBook: (body: CreateBookBody) => request<Book>("/v1/book/createBook", { method: "POST", body }),

  getAllBooks: (params?: Partial<{ sortBy: string; page: number; size: number; ascending: boolean }>) =>
    request<Paged<Book>>(`/v1/book/getAllBooks${buildQuery(params ?? {})}`),

  findBookByIsbn: (isbn: string) => request<Book>(`/v1/book/findBookByIsbn?isbn=${encodeURIComponent(isbn)}`),

  updateBook: (id: string, body: UpdateBookBody) =>
    request<Book>(`/v1/book/updateBook${buildQuery({ id })}`, { method: "POST", body }),

  // Note: Endpoint expects GET with JSON body. fetch supports body on GET, but some intermediaries may not.
  // If this fails in your environment, consider asking backend to accept POST or query parameters for filters.
  searchBooks: (
    filters: SearchBookBody,
    params?: Partial<{ sortBy: string; page: number; size: number; ascending: boolean }>,
  ) =>
    request<Paged<Book>>(`/v1/book/searchBook${buildQuery(params ?? {})}`, {
      method: "GET",
      body: filters, // GET with body per backend spec
    }),

  deleteBook: (id: string) => request<null>(`/v1/book/deleteBook${buildQuery({ id })}`, { method: "DELETE" }),
}

// Borrowings
export const BorrowingsAPI = {
  createBorrowing: (body: { book_id: string; user_id: string }) =>
    request<{
      id: string
      userId?: string
      userName?: string
      bookId?: string
      bookTitle?: string
      expectedReturnDate: string
      actualReturnDate: string | null
      createdAt: string
    }>("/v1/borrowing/createBorrowing/", { method: "POST", body }),

  getAllBorrowing: () => request<Borrowing[]>("/v1/borrowing/getAllBorrowing"),

  returnBorrowing: (borrowing_id: string) =>
    request<Borrowing>(`/v1/borrowing/returnBorrowing${buildQuery({ borrowing_id })}`, { method: "POST", body: {} }),

  getActiveBorrowing: (user_id: string) =>
    request<Borrowing[]>(`/v1/borrowing/getActiveBorrowing${buildQuery({ user_id })}`),
}
