import { useState } from 'react'
import { Pencil, Scissors } from 'lucide-react'
import { AppLayout } from '@/widgets/app-layout'
import { ServiceFormSheet } from '@/features/service/upsert-service'
import { DeleteServiceButton } from '@/features/service/delete-service'
import { useServices, ServiceCard } from '@/entities/service'
import { Button, EmptyState, ErrorState, SkeletonList } from '@/shared/ui'

export default function ServicesPage() {
  const { data, isLoading, isError, refetch } = useServices()
  const [editing, setEditing] = useState(null) // null | 'novo' | service
  const openNew = () => setEditing('novo')

  return (
    <AppLayout title="Serviços" primaryAction={{ label: 'Novo serviço', onClick: openNew }}>
      <div className="mx-auto max-w-3xl">
        {isLoading && <SkeletonList rows={4} />}
        {isError && <ErrorState title="Não foi possível carregar os serviços" onRetry={refetch} />}
        {data?.length === 0 && (
          <EmptyState icon={Scissors} title="Nenhum serviço cadastrado" text="Cadastre seus serviços para poder agendar." action={<Button onClick={openNew}>Cadastrar serviço</Button>} />
        )}
        {data?.length > 0 && (
          <ul className="grid gap-3 lg:grid-cols-2">
            {data.map((s) => (
              <li key={s.id}>
                <ServiceCard
                  service={s}
                  action={
                    <div className="flex gap-2">
                      <Button variant="icon" size="icon" icon={Pencil} aria-label={`Editar ${s.nome}`} onClick={() => setEditing(s)} />
                      <DeleteServiceButton service={s} />
                    </div>
                  }
                />
              </li>
            ))}
          </ul>
        )}
      </div>
      {editing && <ServiceFormSheet open service={editing === 'novo' ? null : editing} onClose={() => setEditing(null)} />}
    </AppLayout>
  )
}
